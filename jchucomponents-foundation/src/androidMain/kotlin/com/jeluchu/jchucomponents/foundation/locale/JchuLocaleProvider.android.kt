package com.jeluchu.jchucomponents.foundation.locale

import java.util.Locale

actual object JchuLocaleProvider {
    actual fun getLanguage(): String = Locale.getDefault().language

    actual fun getPlatformLanguageCode(): String {
        val locale = Locale.getDefault()
        return if (locale.language == "es" && locale.country == "US") "es-US" else locale.language
    }

    actual fun getLanguageTag(): String = Locale.getDefault().toLanguageTag()
}
