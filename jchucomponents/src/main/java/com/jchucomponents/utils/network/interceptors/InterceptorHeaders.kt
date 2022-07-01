/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jchucomponents.utils.network.interceptors

import com.jchucomponents.core.extensions.strings.empty
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

