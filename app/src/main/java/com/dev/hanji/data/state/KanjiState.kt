package com.dev.hanji.data.state

import androidx.paging.PagingData
import com.dev.hanji.data.api.JishoResponse
import com.dev.hanji.data.model.KanjiEntity

data class KanjiState(
    val kanjiList: PagingData<KanjiEntity> = PagingData.empty(),
    val character: KanjiEntity? = null,
    val jisho: JishoResponse? = null,
    override val attempts: List<UserAttempt>? = null

): HasAttempts {
        override val total: Long
        get() = attempts?.sumOf { it.attempt ?: 0 } ?: 0
}
