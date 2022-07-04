/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package  com.jeluchu.jchucomponentscompose.utils

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.json.JSONObject
import java.io.IOException

class JsonConverterObjectToString : TypeAdapter<String?>() {

    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: String?) {
        runCatching {
            val jsonObject = JSONObject(value.orEmpty())
            out.beginObject()
            val iterator = jsonObject.keys()
            while (iterator.hasNext()) {
                val key = iterator.next()
                val keyValue = jsonObject.getString(key)
                out.name(key)
                    .value(keyValue)
            }
            out.endObject()
        }.getOrElse { it.printStackTrace() }
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): String {
        `in`.beginObject()
        val jsonObject = JSONObject()
        while (`in`.hasNext()) {
            val name: String = `in`.nextName()
            val value: String = `in`.nextString()
            runCatching {
                jsonObject.put(name, value)
            }.getOrElse { it.printStackTrace() }
        }
        `in`.endObject()
        return jsonObject.toString()
    }

}