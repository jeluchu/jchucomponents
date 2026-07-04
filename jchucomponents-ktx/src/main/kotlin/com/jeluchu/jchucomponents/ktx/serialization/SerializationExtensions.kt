/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.serialization

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

val json: Json by lazy {
    Json {
        ignoreUnknownKeys = true
    }
}

/** Converts a JSON string to a serializable object. */
inline fun <reified T> String.toObject(): T = json.decodeFromString(this)

/** Converts a JSON object to a serializable object. */
inline fun <reified T> JsonObject.toObject(): T = json.decodeFromJsonElement(this)

/** Converts a serializable object to a JSON object. */
inline fun <reified T> T.toMap(): JsonObject = json.encodeToJsonElement(this).jsonObject

/** Converts a serializable object of type [T] to another serializable type [R]. */
inline fun <reified T, reified R> T.convert(): R =
    json.decodeFromJsonElement(json.encodeToJsonElement(this))

/** Decodes JSON, returning `null` when the input is null or invalid. */
inline fun <reified T> Json.decodeOrNull(value: String?): T? =
    value?.let { runCatching { decodeFromString<T>(it) }.getOrNull() }

/** Decodes a JSON list, returning `null` when the input is null or invalid. */
inline fun <reified T> Json.decodeListOrNull(value: String?): List<T>? =
    decodeOrNull<List<T>>(value)
