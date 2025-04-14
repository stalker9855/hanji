package com.dev.hanji.data.state

import androidx.paging.PagingData
import com.dev.hanji.data.dao.KanjiWithAttemptStatus

data class KanjiProgressState(
    val progress: PagingData<KanjiWithAttemptStatus> = PagingData.empty()
)