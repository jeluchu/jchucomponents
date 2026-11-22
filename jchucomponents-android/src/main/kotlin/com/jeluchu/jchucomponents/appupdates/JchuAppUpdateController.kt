package com.jeluchu.jchucomponents.appupdates

import android.app.Activity
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages Google Play's flexible in-app update flow for one [ComponentActivity].
 *
 * Create this controller during the activity's `onCreate` callback. It registers its activity
 * result and observes the activity lifecycle; the host app remains responsible for rendering
 * [JchuAppUpdateSnapshot] in its own UI.
 *
 * This controller is opt-in. Set [JchuAppUpdateConfig.enabled] to false for builds that should not
 * check Google Play for updates.
 */
public class JchuAppUpdateController(
    private val activity: ComponentActivity,
    public val config: JchuAppUpdateConfig = JchuAppUpdateConfig(),
) : DefaultLifecycleObserver {
    private val appUpdateManager: AppUpdateManager? =
        if (config.enabled) AppUpdateManagerFactory.create(activity) else null

    private val preferences = activity.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    private val mutableSnapshot = MutableStateFlow(JchuAppUpdateSnapshot())
    private var shouldAutoShowUpdateUi = true
    private var latestUpdateInfo: AppUpdateInfo? = null
    private var currentVersionCode: Int? = null
    private var isCheckingForUpdate = false
    private var updateFlowInProgress = false
    private var listenerRegistered = false

    private val updateFlowLauncher: ActivityResultLauncher<IntentSenderRequest>? =
        if (config.enabled) {
            activity.activityResultRegistry.register(
                ACTIVITY_RESULT_KEY,
                activity,
                ActivityResultContracts.StartIntentSenderForResult(),
            ) { result ->
                onUpdateFlowResult(result.resultCode)
            }
        } else {
            null
        }

    private val installStateListener = InstallStateUpdatedListener(::onInstallStateUpdated)

    /** Observable snapshots for UI frameworks such as Jetpack Compose. */
    public val snapshots: StateFlow<JchuAppUpdateSnapshot> = mutableSnapshot.asStateFlow()

    /** The latest snapshot, also available before the first update check completes. */
    public val snapshot: JchuAppUpdateSnapshot
        get() = mutableSnapshot.value

    init {
        if (config.enabled) {
            activity.lifecycle.addObserver(this)
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        val manager = appUpdateManager ?: return
        if (listenerRegistered) return
        manager.registerListener(installStateListener)
        listenerRegistered = true
    }

    override fun onResume(owner: LifecycleOwner) {
        checkForUpdate()
    }

    override fun onStop(owner: LifecycleOwner) {
        unregisterInstallStateListener()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        unregisterInstallStateListener()
    }

    /** Starts the flexible update flow for the latest available update. */
    public fun startFlexibleUpdate() {
        val manager = appUpdateManager ?: return
        val launcher = updateFlowLauncher ?: return
        val updateInfo = latestUpdateInfo
        if (updateInfo == null || !updateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
            checkForUpdate(forcePrompt = true)
            return
        }

        updateFlowInProgress = true
        shouldAutoShowUpdateUi = true
        publish(JchuAppUpdateState.Preparing, shouldShowUi = false)
        try {
            val flowStarted = manager.startUpdateFlowForResult(
                updateInfo,
                launcher,
                AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build(),
            )
            latestUpdateInfo = null
            if (!flowStarted) {
                updateFlowInProgress = false
                publish(JchuAppUpdateState.Failed, shouldShowUi = true)
            }
        } catch (_: Exception) {
            updateFlowInProgress = false
            latestUpdateInfo = null
            publish(JchuAppUpdateState.Failed, shouldShowUi = true)
        }
    }

    /** Completes installation after the flexible update has reached [JchuAppUpdateState.Downloaded]. */
    public fun installDownloadedUpdate() {
        val manager = appUpdateManager ?: return
        manager.completeUpdate()
            .addOnFailureListener {
                if (!activity.isFinishing && !activity.isDestroyed) {
                    publish(JchuAppUpdateState.Failed, shouldShowUi = true)
                }
            }
    }

    /** Checks again and requests that an available update be presented even if recently dismissed. */
    public fun retry() {
        if (appUpdateManager == null) return
        shouldAutoShowUpdateUi = true
        checkForUpdate(forcePrompt = true)
    }

    /** Dismisses the current update UI and applies the configured cooldown to available updates. */
    public fun dismiss() {
        if (snapshot.state is JchuAppUpdateState.Available ||
            snapshot.state is JchuAppUpdateState.Failed
        ) {
            currentVersionCode?.let(::recordPromptDismissal)
        }
        shouldAutoShowUpdateUi = false
        publish(
            state = if (snapshot.state is JchuAppUpdateState.Available) {
                JchuAppUpdateState.Hidden
            } else {
                snapshot.state
            },
            shouldShowUi = false,
        )
    }

    /** Reopens the app's update UI for an update that is still in progress. */
    public fun showDetails() {
        if (snapshot.state !is JchuAppUpdateState.Hidden) {
            publish(snapshot.state, shouldShowUi = true)
        }
    }

    private fun checkForUpdate(forcePrompt: Boolean = false) {
        val manager = appUpdateManager ?: return
        if (
            isCheckingForUpdate ||
            updateFlowInProgress ||
            activity.isFinishing ||
            activity.isDestroyed
        ) {
            return
        }

        isCheckingForUpdate = true
        manager.appUpdateInfo
            .addOnSuccessListener { updateInfo ->
                if (!activity.isFinishing && !activity.isDestroyed) {
                    latestUpdateInfo = updateInfo
                    applyUpdateInfo(updateInfo, forcePrompt)
                }
            }
            .addOnFailureListener {
                if (
                    !activity.isFinishing &&
                    !activity.isDestroyed &&
                    snapshot.state is JchuAppUpdateState.Preparing
                ) {
                    publish(JchuAppUpdateState.Failed, shouldShowUi = true)
                }
            }
            .addOnCompleteListener {
                isCheckingForUpdate = false
            }
    }

    private fun applyUpdateInfo(updateInfo: AppUpdateInfo, forcePrompt: Boolean) {
        val flexibleUpdateAvailable =
            updateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                updateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)

        when (updateInfo.installStatus()) {
            InstallStatus.PENDING -> publish(
                JchuAppUpdateState.Preparing,
                shouldShowUi = shouldAutoShowUpdateUi,
            )

            InstallStatus.DOWNLOADING -> publish(
                JchuAppUpdateState.Downloading(updateProgress(updateInfo)),
                shouldShowUi = shouldAutoShowUpdateUi,
            )

            InstallStatus.DOWNLOADED -> publish(
                JchuAppUpdateState.Downloaded,
                shouldShowUi = shouldAutoShowUpdateUi,
            )

            InstallStatus.INSTALLING -> publish(
                JchuAppUpdateState.Installing,
                shouldShowUi = shouldAutoShowUpdateUi,
            )

            InstallStatus.FAILED -> {
                if (forcePrompt && flexibleUpdateAvailable) {
                    showAvailableUpdate(updateInfo, forcePrompt = true)
                } else {
                    publish(JchuAppUpdateState.Failed, shouldShowUi = shouldAutoShowUpdateUi)
                }
            }

            InstallStatus.CANCELED -> {
                if (flexibleUpdateAvailable) {
                    showAvailableUpdate(updateInfo, forcePrompt)
                } else {
                    publish(JchuAppUpdateState.Hidden, shouldShowUi = false)
                }
            }

            InstallStatus.INSTALLED -> {
                latestUpdateInfo = null
                currentVersionCode = null
                publish(JchuAppUpdateState.Hidden, shouldShowUi = false)
            }

            else -> {
                if (flexibleUpdateAvailable) {
                    showAvailableUpdate(
                        updateInfo = updateInfo,
                        forcePrompt = forcePrompt,
                        preservePreparingState = true,
                    )
                } else if (snapshot.state !is JchuAppUpdateState.Downloading) {
                    latestUpdateInfo = null
                    currentVersionCode = null
                    publish(JchuAppUpdateState.Hidden, shouldShowUi = false)
                }
            }
        }
    }

    private fun showAvailableUpdate(
        updateInfo: AppUpdateInfo,
        forcePrompt: Boolean,
        preservePreparingState: Boolean = false,
    ) {
        val versionCode = updateInfo.availableVersionCode()
        if (currentVersionCode != versionCode) shouldAutoShowUpdateUi = true
        currentVersionCode = versionCode
        if (preservePreparingState && snapshot.state is JchuAppUpdateState.Preparing) return

        if (forcePrompt || shouldShowPrompt(versionCode)) {
            publish(JchuAppUpdateState.Available, shouldShowUi = true)
        } else {
            publish(JchuAppUpdateState.Hidden, shouldShowUi = false)
        }
    }

    private fun onInstallStateUpdated(installState: InstallState) {
        when (installState.installStatus()) {
            InstallStatus.PENDING -> publish(
                JchuAppUpdateState.Preparing,
                shouldShowUi = snapshot.shouldShowUi,
            )

            InstallStatus.DOWNLOADING -> {
                val totalBytes = installState.totalBytesToDownload()
                val progress = if (totalBytes > 0L) {
                    (installState.bytesDownloaded().toFloat() / totalBytes.toFloat()).coerceIn(0f, 1f)
                } else {
                    null
                }
                publish(
                    JchuAppUpdateState.Downloading(progress),
                    shouldShowUi = snapshot.shouldShowUi,
                )
            }

            InstallStatus.DOWNLOADED -> publish(
                JchuAppUpdateState.Downloaded,
                shouldShowUi = shouldAutoShowUpdateUi,
            )

            InstallStatus.INSTALLING -> publish(
                JchuAppUpdateState.Installing,
                shouldShowUi = snapshot.shouldShowUi,
            )

            InstallStatus.INSTALLED -> {
                latestUpdateInfo = null
                currentVersionCode = null
                publish(JchuAppUpdateState.Hidden, shouldShowUi = false)
            }

            InstallStatus.FAILED, InstallStatus.CANCELED -> publish(
                JchuAppUpdateState.Failed,
                shouldShowUi = shouldAutoShowUpdateUi,
            )

            else -> Unit
        }
    }

    private fun onUpdateFlowResult(resultCode: Int) {
        updateFlowInProgress = false
        when (resultCode) {
            Activity.RESULT_OK -> {
                shouldAutoShowUpdateUi = true
                publish(JchuAppUpdateState.Preparing, shouldShowUi = true)
            }

            Activity.RESULT_CANCELED -> {
                currentVersionCode?.let(::recordPromptDismissal)
                shouldAutoShowUpdateUi = false
                latestUpdateInfo = null
                publish(JchuAppUpdateState.Hidden, shouldShowUi = false)
            }

            else -> publish(JchuAppUpdateState.Failed, shouldShowUi = true)
        }
    }

    private fun updateProgress(updateInfo: AppUpdateInfo): Float? {
        val totalBytes = updateInfo.totalBytesToDownload()
        return if (totalBytes > 0L) {
            (updateInfo.bytesDownloaded().toFloat() / totalBytes.toFloat()).coerceIn(0f, 1f)
        } else {
            null
        }
    }

    private fun shouldShowPrompt(versionCode: Int): Boolean {
        val dismissedVersion = preferences.getInt(KEY_DISMISSED_VERSION, NO_VERSION)
        val dismissedAt = preferences.getLong(KEY_DISMISSED_AT, 0L)
        return dismissedVersion != versionCode ||
            System.currentTimeMillis() - dismissedAt >= config.promptCooldownMillis
    }

    private fun recordPromptDismissal(versionCode: Int) {
        preferences.edit()
            .putInt(KEY_DISMISSED_VERSION, versionCode)
            .putLong(KEY_DISMISSED_AT, System.currentTimeMillis())
            .apply()
    }

    private fun publish(state: JchuAppUpdateState, shouldShowUi: Boolean) {
        mutableSnapshot.value = JchuAppUpdateSnapshot(state = state, shouldShowUi = shouldShowUi)
    }

    private fun unregisterInstallStateListener() {
        val manager = appUpdateManager ?: return
        if (!listenerRegistered) return
        manager.unregisterListener(installStateListener)
        listenerRegistered = false
    }

    private companion object {
        const val ACTIVITY_RESULT_KEY = "jchucomponents.app_updates.in_app_update"
        const val PREFERENCES_NAME = "in_app_update_preferences"
        const val KEY_DISMISSED_VERSION = "dismissed_version_code"
        const val KEY_DISMISSED_AT = "dismissed_at"
        const val NO_VERSION = -1
    }
}
