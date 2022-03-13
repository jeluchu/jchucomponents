package com.jeluchu.jchucomponentscompose.utils.network.interceptors

import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response
import java.text.Normalizer
import java.util.*

class Interceptor(
    private val interceptorHeaders: InterceptorHeaders
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val builder = chain.request().newBuilder()
            .addHeader(
                "User-Agent",
                "${interceptorHeaders.userAgent.appName}/${interceptorHeaders.userAgent.versionName} (rv ${interceptorHeaders.userAgent.versionCode}) okhttp/${interceptorHeaders.userAgent.okhttpVersion}"
            )
            .addHeader("X-Client", "${interceptorHeaders.client}-android")
            .addHeader("Accept", "application/json")
            .addHeader("Accept-Language", Locale.getDefault().toString().replace("_", "-"))
            .addHeader("X-Request-AppVersion", interceptorHeaders.userAgent.versionName)
            .addHeader("X-Request-OsVersion", osVersion)
            .addHeader("X-Request-Device", cleanInvalidCharacters(deviceName))
            .addHeader("X-Mobile-Native", "Android")
            .addHeader("X-User-TimezoneOffset", Date().toString())
        if (interceptorHeaders.key.isNotEmpty()) {
            builder.addHeader(interceptorHeaders.keyHeaderAgent, interceptorHeaders.key)
        }

        return chain.proceed(builder.build())

    }

    private fun cleanInvalidCharacters(`in`: String): String {
        var subjectString = `in`
        subjectString = Normalizer.normalize(subjectString, Normalizer.Form.NFD)
        return subjectString.replace("[^\\x00-\\x7F]".toRegex(), "")
    }

    private val deviceName: String
        get() {
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            return if (model.startsWith(manufacturer)) model else "$manufacturer $model"
        }

    private val osVersion: String
        get() = "Android " + Build.VERSION.RELEASE + " (" + Build.VERSION.SDK_INT + ")"

}