package com.jeluchu.jchucomponents.ktx.any

import android.util.Log
import com.jeluchu.jchucomponents.ktx.constants.JCHUCOMPONENTS_ERROR
import com.jeluchu.jchucomponents.foundation.serialization.defaultJson

val Any.logTag: String
    get() = this::class.java.simpleName

/**
 *
 *
 * Ex: object.toJson() ?: "" / object.toJson().orEmpty()
 *
 **/
inline fun <reified T> T.toJson(): String? =
    runCatching { defaultJson.encodeToString(this) }
        .onFailure { Log.e(JCHUCOMPONENTS_ERROR, it.message.orEmpty()) }
        .getOrNull()

/**
 *
 * Below method uses generics and can convert JSONString
 * to Any type of object depending on the type provided
 *
 * Ex: json.fromJson<Object>()
 *
 **/
inline fun <reified T> String.fromJson(): T? {
    if (this.isEmpty()) return null
    return runCatching { defaultJson.decodeFromString<T>(this) }
        .onFailure { Log.e(JCHUCOMPONENTS_ERROR, it.message.orEmpty()) }
        .getOrNull()
}
