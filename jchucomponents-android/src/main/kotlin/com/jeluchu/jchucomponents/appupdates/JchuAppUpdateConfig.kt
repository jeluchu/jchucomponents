package com.jeluchu.jchucomponents.appupdates

/** Configuration for the optional Google Play in-app update integration. */
public data class JchuAppUpdateConfig(
    /** Set to false for builds that are not distributed through Google Play. */
    public val enabled: Boolean = true,
    /** Minimum time before a dismissed available-update prompt can be shown again. */
    public val promptCooldownMillis: Long = DEFAULT_PROMPT_COOLDOWN_MILLIS,
) {
    init {
        require(promptCooldownMillis >= 0L) {
            "promptCooldownMillis must be zero or greater."
        }
    }

    public companion object {
        /** The default prompt cooldown, matching the original La Arroba behavior. */
        public const val DEFAULT_PROMPT_COOLDOWN_MILLIS: Long = 24L * 60L * 60L * 1000L
    }
}
