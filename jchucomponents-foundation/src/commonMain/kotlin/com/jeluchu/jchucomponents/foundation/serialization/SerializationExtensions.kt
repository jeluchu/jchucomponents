package com.jeluchu.jchucomponents.foundation.serialization

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

val defaultJson: Json by lazy {
    Json {
        ignoreUnknownKeys = true
    }
}

inline fun <reified T> String.toObject(json: Json = defaultJson): T = json.decodeFromString(this)

inline fun <reified T> JsonObject.toObject(json: Json = defaultJson): T = json.decodeFromJsonElement(this)

inline fun <reified T> T.toJsonObject(json: Json = defaultJson): JsonObject = json.encodeToJsonElement(this).jsonObject

inline fun <reified T, reified R> T.convert(json: Json = defaultJson): R = json.decodeFromJsonElement(json.encodeToJsonElement(this))

inline fun <reified T> Json.decodeOrNull(value: String?): T? = value?.let { runCatching { decodeFromString<T>(it) }.getOrNull() }

inline fun <reified T> Json.decodeListOrNull(value: String?): List<T>? = decodeOrNull<List<T>>(value)
