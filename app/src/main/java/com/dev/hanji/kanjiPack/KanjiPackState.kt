package com.dev.hanji.kanjiPack

import com.dev.hanji.kanji.KanjiEntity

data class KanjiPackState(
    val kanjiList: List<KanjiEntity> = emptyList(),
    val kanjiPacks: List<KanjiPackEntity> = emptyList(),
    val name: String = "",
    val description: String = "",
    val userId: Int = 1,
)