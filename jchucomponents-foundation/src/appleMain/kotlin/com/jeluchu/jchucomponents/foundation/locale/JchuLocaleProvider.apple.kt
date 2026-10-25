package com.jeluchu.jchucomponents.foundation.locale

import platform.Foundation.NSLocale
import platform.Foundation.preferredLanguages

actual object JchuLocaleProvider {
    private fun preferredLanguage(): String =
        NSLocale.preferredLanguages.firstOrNull() as? String ?: "en"

    actual fun getLanguage(): String = preferredLanguage()

    actual fun getPlatformLanguageCode(): String {
        val preferredLanguage = preferredLanguage()
        if (preferredLanguage.startsWith("es-US")) return "es-US"
        return preferredLanguage.substringBefore("-")
    }

    actual fun getLanguageTag(): String = preferredLanguage()
}
