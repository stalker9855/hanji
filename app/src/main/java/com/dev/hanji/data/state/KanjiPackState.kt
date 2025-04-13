package com.dev.hanji.data.state

import androidx.paging.PagingData
import com.dev.hanji.data.model.KanjiPackEntity
import com.dev.hanji.data.model.PackWithKanji
import com.dev.hanji.data.model.KanjiEntity

data class KanjiPackState(
    val kanjiList: List<KanjiEntity> = emptyList(),
    val kanjiPacks: List<KanjiPackEntity> = emptyList(),
)
data class KanjiPackStateById(
    val packId: Long? = 0,
    val kanjiPackWithKanjiList: PackWithKanji? = null,
)
data class CreateEditKanjiPackState(
    val packId: Long? = null,
    val selectedKanjiList: List<KanjiEntity> = emptyList(),
    val availableKanjiList: List<KanjiEntity> = emptyList(),
    val pagedKanjiList: PagingData<KanjiEntity> = PagingData.empty(),
    val searchQuery: String = "",
    val title: String = "",
    val name: String = "",
    val description: String = "",
    val userId: Int = 1,
)

