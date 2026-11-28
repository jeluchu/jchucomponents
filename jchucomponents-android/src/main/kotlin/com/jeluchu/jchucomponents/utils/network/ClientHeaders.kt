/*
 *
 *  Copyright 2026 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.utils.network

/**
 * Describes the client app (user agent and API key) for building request headers.
 */
public data class ClientHeaders(
    public val userAgent: UserAgent,
    public val keyHeader: String = "",
    public val key: String = "",
    public val client: String
) {
    public data class UserAgent(
        public val appName: String,
        public val versionName: String,
        public val versionCode: Int
    )
}
