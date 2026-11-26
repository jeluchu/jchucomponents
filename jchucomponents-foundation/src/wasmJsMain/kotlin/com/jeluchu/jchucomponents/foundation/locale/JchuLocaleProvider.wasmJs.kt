package com.jeluchu.jchucomponents.foundation.locale

private fun browserLanguageTag(): String = js("(navigator.language || 'en')")

actual object JchuLocaleProvider {
    actual fun getLanguage(): String = getLanguageTag().substringBefore('-')

    actual fun getPlatformLanguageCode(): String {
        val tag = getLanguageTag()
        return if (tag.equals("es-US", ignoreCase = true)) "es-US" else tag.substringBefore('-')
    }

    actual fun getLanguageTag(): String = browserLanguageTag()
}
