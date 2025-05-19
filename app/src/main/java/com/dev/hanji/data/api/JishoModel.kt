package com.dev.hanji.data.api


import com.google.gson.annotations.SerializedName

data class JishoResponse(
    val meta: Meta,
    val data: List<KanjiEntry>
)

data class Meta(
    val status: Int
)

data class KanjiEntry(
    val slug: String,
    @SerializedName("is_common") val isCommon: Boolean,
    val tags: List<String>,
    val jlpt: List<String>,
    val japanese: List<JapaneseEntry>,
    val senses: List<Sense>,
    val attribution: Attribution
)

data class JapaneseEntry(
    val word: String?,
    val reading: String
)

data class Sense(
    @SerializedName("english_definitions") val englishDefinitions: List<String>,
    @SerializedName("parts_of_speech") val partsOfSpeech: List<String>,
    val links: List<Link> = emptyList(),
    val tags: List<String> = emptyList(),
    val restrictions: List<String> = emptyList(),
    @SerializedName("see_also") val seeAlso: List<String> = emptyList(),
    val antonyms: List<String> = emptyList(),
    val source: List<String> = emptyList(),
    val info: List<String> = emptyList(),
    val sentences: List<String>? = null
)

data class Link(
    val text: String,
    val url: String
)

data class Attribution(
    val jmdict: Boolean,
    val jmnedict: Boolean,
    val dbpedia: Any?
)

