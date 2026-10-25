package com.jeluchu.jchucomponents.foundation.locale

/** Reads the system locale using the behavior shared by the current JchuComponents apps. */
expect object JchuLocaleProvider {
    /**
     * Returns the platform's preferred language value.
     *
     * Android returns the primary language subtag (for example, `es`). Apple platforms return
     * the first preferred language tag (for example, `es-ES`), matching the existing app behavior.
     */
    fun getLanguage(): String

    /**
     * Returns the language code used to select app translations.
     *
     * Spanish in the United States maps to `es-US`; other regional tags map to their language
     * subtag so existing translation keys such as `es` continue to resolve.
     */
    fun getPlatformLanguageCode(): String

    /** Returns the full BCP 47 tag for the current locale or first preferred language. */
    fun getLanguageTag(): String
}
