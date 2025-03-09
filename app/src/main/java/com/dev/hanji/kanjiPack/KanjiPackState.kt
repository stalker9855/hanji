package com.dev.hanji.kanjiPack

import com.dev.hanji.kanji.KanjiEntity

data class KanjiPackState(
    val kanjiList: List<KanjiEntity> = emptyList(),
    val kanjiPacks: List<KanjiPackEntity> = emptyList(),
)
data class KanjiPackStateById(
    val packId: Int = 0,
    val kanjiPackWithKanjiList: PackWithKanji? = null,
)
data class CreateKanjiPackState(
    val selectedKanjiList: List<KanjiEntity> = emptyList(),
    val availableKanjiList: List<KanjiEntity> = emptyList(),
    val name: String = "",
    val description: String = "",
    val userId: Int = 1,
)
