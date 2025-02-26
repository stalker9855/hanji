package com.dev.hanji.desirializers

import com.dev.hanji.kanji.KanjiEntity
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type

class KanjiEntityDeserializer : JsonDeserializer<KanjiEntity> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): KanjiEntity {
        val jsonObject = json.asJsonObject

        return KanjiEntity(
            character = jsonObject.get("character").asString,
            strokes = jsonObject.get("strokes").asInt,
            meanings = parseList(jsonObject, "meanings"),
            readingsOn = parseList(jsonObject, "readings_on"),
            readingsKun = parseList(jsonObject, "readings_kun"),
            unicodeHex = jsonObject.get("unicode_hex").asString
        )
    }

    private fun parseList(jsonObject: JsonObject, key: String): List<String> {
        return when {
            jsonObject.has(key) -> {
                val element = jsonObject.get(key)
                if (element.isJsonArray) {
                    element.asJsonArray.map { it.asString }
                } else {
                    element.asString.split(", ").map { it.trim() }
                }
            }
            else -> emptyList()
        }
    }
}

