/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.utils.network.interceptors

import okhttp3.OkHttp

data class InterceptorHeaders(
    val userAgent: UserAgent,
    val keyHeaderAgent: String = "",
    val key: String = "",
    val client: String
) {

    data class UserAgent(
        val appName: String,
        val versionName: String,
        val versionCode: Int,
        val okhttpVersion: String = OkHttp.VERSION
    )

}

