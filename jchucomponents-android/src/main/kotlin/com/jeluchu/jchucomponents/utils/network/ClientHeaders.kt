/*
 *
 *  Copyright 2026 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.utils.network

/**
 * Headers shared by every request created with [KtorClient].
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
