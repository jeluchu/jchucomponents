package com.jeluchu.jchucomponents.appupdates

/** Current state of a Google Play flexible in-app update. */
public sealed interface JchuAppUpdateState {
    /** There is no update to present, or the available update was dismissed for now. */
    public data object Hidden : JchuAppUpdateState

    /** Google Play has an update that can be installed with the flexible flow. */
    public data object Available : JchuAppUpdateState

    /** Google Play is preparing the update flow. */
    public data object Preparing : JchuAppUpdateState

    /** The update is downloading. Progress is a value from 0.0 to 1.0 when available. */
    public data class Downloading(public val progress: Float?) : JchuAppUpdateState

    /** The update has downloaded and can be installed by calling [JchuAppUpdateController.installDownloadedUpdate]. */
    public data object Downloaded : JchuAppUpdateState

    /** Google Play is installing the downloaded update. */
    public data object Installing : JchuAppUpdateState

    /** The update flow failed or was cancelled and can be retried. */
    public data object Failed : JchuAppUpdateState
}

/** State and a presentation hint for an app's own update UI. */
public data class JchuAppUpdateSnapshot(
    public val state: JchuAppUpdateState = JchuAppUpdateState.Hidden,
    /** Whether the host app should present its update UI for this state. */
    public val shouldShowUi: Boolean = false,
)
