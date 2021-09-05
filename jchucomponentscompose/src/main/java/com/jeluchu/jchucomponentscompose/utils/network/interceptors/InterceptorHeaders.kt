package com.jeluchu.jchucomponentscompose.utils.network.interceptors

import com.jeluchu.jchucomponentscompose.core.extensions.strings.empty
import okhttp3.OkHttp

data class InterceptorHeaders(
    val userAgent: UserAgent,
    val keyHeaderAgent: String = String.empty(),
    val key: String = String.empty(),
    val client: String
) {

    data class UserAgent(
        val appName: String,
        val versionName: String,
        val versionCode: Int,
        val okhttpVersion: String = OkHttp.VERSION
    )

}

