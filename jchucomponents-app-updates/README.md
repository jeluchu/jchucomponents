# JchuComponents App Updates

Android-only management for Google Play flexible in-app updates. The module
checks availability, launches and tracks the Play update flow, and publishes
state snapshots as a `StateFlow`. It does not provide UI; each app decides how
to present those states and actions.

## Dependency

```kotlin
dependencies {
    implementation("io.github.jeluchu:jchucomponents-app-updates:3.0.0-alpha21")
}
```

## Connect an activity

Create one controller in `onCreate`, before the activity reaches `STARTED`.
Choose the `enabled` value from the app's Google Play distribution variant.

```kotlin
private lateinit var appUpdates: JchuAppUpdateController

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    appUpdates = JchuAppUpdateController(
        activity = this,
        config = JchuAppUpdateConfig(enabled = BuildConfig.PLAY_STORE_UPDATES_ENABLED),
    )
}
```

The controller registers the Activity Result callback and observes `onStart`,
`onResume`, and `onStop`. The host app does not need to forward those lifecycle
events.

## Render the state in the app

The app can use any UI framework or layout. For example, a Compose screen can
render its own buttons and progress indicators from the snapshot:

```kotlin
import androidx.lifecycle.compose.collectAsStateWithLifecycle

setContent {
    val updateSnapshot by appUpdates.snapshots.collectAsStateWithLifecycle()

    if (updateSnapshot.shouldShowUi) {
        when (val state = updateSnapshot.state) {
            JchuAppUpdateState.Available -> UpdateNotice(
                onUpdate = appUpdates::startFlexibleUpdate,
                onDismiss = appUpdates::dismiss,
            )

            is JchuAppUpdateState.Downloading -> UpdateProgress(state.progress)

            JchuAppUpdateState.Downloaded -> UpdateReady(
                onInstall = appUpdates::installDownloadedUpdate,
            )

            JchuAppUpdateState.Preparing,
            JchuAppUpdateState.Installing -> UpdateInProgress()

            JchuAppUpdateState.Failed -> UpdateFailure(
                onRetry = appUpdates::retry,
                onDismiss = appUpdates::dismiss,
            )

            JchuAppUpdateState.Hidden -> Unit
        }
    }
}
```

`collectAsStateWithLifecycle` comes from `androidx.lifecycle:lifecycle-runtime-compose`,
which keeps collection active only while the Compose screen is in an active lifecycle state.

`JchuAppUpdateConfig.promptCooldownMillis` controls how long an available update
stays dismissed before the controller presents it again. The default is 24
hours; set it to `0` to show it on every eligible check.
