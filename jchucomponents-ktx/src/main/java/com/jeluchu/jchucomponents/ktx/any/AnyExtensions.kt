package com.jeluchu.jchucomponents.ktx.any

import android.util.Log
import com.jeluchu.jchucomponents.ktx.constants.JCHUCOMPONENTS_ERROR
import com.jeluchu.jchucomponents.ktx.gson.gson
import java.lang.reflect.Type

fun Any?.isNull() = this == null

/**
 *
 *
 * Ex: object.toJson() ?: "" / object.toJson().orEmpty()
 *
 **/
fun Any.toJson(): String? {
    return try {
        gson.toJson(this)
    } catch (exception: Exception) {
        Log.e(JCHUCOMPONENTS_ERROR, exception.message.orEmpty())
        null
    }
}

/**
 *
 * Below method uses generics and can convert JSONString
 * to Any type of object depending on the type provided
 *
 * Ex: json.fromJson<Object>(Object::class.java)
 *
 **/
fun <T> String.fromJson(type: Type): T? {
    if (this.isEmpty()) return null
    return try {
        gson.fromJson<T>(this, type)
    } catch (exception: Exception) {
        Log.e(JCHUCOMPONENTS_ERROR, exception.message.orEmpty())
        null
    }
}