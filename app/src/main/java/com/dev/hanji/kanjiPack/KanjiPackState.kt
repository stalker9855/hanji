package com.dev.hanji.kanjiPack

import com.dev.hanji.kanji.KanjiEntity

data class KanjiPackState(
    val name: String = "",
    val description: String = "",
    val kanji: List<KanjiEntity> = emptyList()
    // userId
)