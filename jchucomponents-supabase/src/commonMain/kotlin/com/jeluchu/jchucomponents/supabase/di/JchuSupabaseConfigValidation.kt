package com.jeluchu.jchucomponents.supabase.di

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.io.encoding.Base64

private val localHosts = listOf("http://localhost", "http://127.0.0.1", "http://10.0.2.2", "http://[::1]")

/** Returns why this public configuration cannot be used by a client app, or `null` when it is fine. */
internal fun jchuSupabaseConfigProblem(
    url: String,
    publishableKey: String
): String? =
    when {
        url.isBlank() || publishableKey.isBlank() -> "url and publishableKey are required"
        publishableKey.startsWith("YOUR_") -> "publishableKey is still a placeholder"
        publishableKey.startsWith("sb_secret_") -> "publishableKey is a secret key and must never ship in an app"
        publishableKey.isServiceRoleJwt() -> "publishableKey is a service_role key and must never ship in an app"
        !url.startsWith("https://") && localHosts.none(url::startsWith) -> "url must use https"
        else -> null
    }

/** True when this API key is a Supabase JWT carrying the privileged `service_role` role. */
internal fun String.isServiceRoleJwt(): Boolean {
    val payloadSegment = split('.').takeIf { it.size == 3 }?.get(1) ?: return false
    return runCatching {
        val payload =
            Base64.UrlSafe
                .withPadding(Base64.PaddingOption.ABSENT_OPTIONAL)
                .decode(payloadSegment)
                .decodeToString()
        Json.parseToJsonElement(payload).jsonObject["role"]?.jsonPrimitive?.content == "service_role"
    }.getOrDefault(false)
}
