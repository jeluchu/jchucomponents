/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.serialization

import com.jeluchu.jchucomponents.foundation.serialization.convert as foundationConvert
import com.jeluchu.jchucomponents.foundation.serialization.defaultJson
import com.jeluchu.jchucomponents.foundation.serialization.decodeListOrNull as foundationDecodeListOrNull
import com.jeluchu.jchucomponents.foundation.serialization.decodeOrNull as foundationDecodeOrNull
import com.jeluchu.jchucomponents.foundation.serialization.toJsonObject as foundationToJsonObject
import com.jeluchu.jchucomponents.foundation.serialization.toObject as foundationToObject
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

val json: Json
    get() = defaultJson

/** Converts a JSON string to a serializable object. */
inline fun <reified T> String.toObject(): T = foundationToObject(json)

/** Converts a JSON object to a serializable object. */
inline fun <reified T> JsonObject.toObject(): T = foundationToObject(json)

/** Converts a serializable object to a JSON object. */
inline fun <reified T> T.toMap(): JsonObject = foundationToJsonObject(json)

/** Converts a serializable object of type [T] to another serializable type [R]. */
inline fun <reified T, reified R> T.convert(): R = foundationConvert(json)

/** Decodes JSON, returning `null` when the input is null or invalid. */
inline fun <reified T> Json.decodeOrNull(value: String?): T? = foundationDecodeOrNull(value)

/** Decodes a JSON list, returning `null` when the input is null or invalid. */
inline fun <reified T> Json.decodeListOrNull(value: String?): List<T>? = foundationDecodeListOrNull(value)
