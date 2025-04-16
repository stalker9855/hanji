package com.dev.hanji.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.dev.hanji.data.dao.ProgressDao
import com.dev.hanji.data.state.InitialProgressState
import com.dev.hanji.data.state.KanjiProgressState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ProgressViewModel(private val dao: ProgressDao): ViewModel() {
    private val _progressState = MutableStateFlow(KanjiProgressState())

    private val _attempts = dao.getAttempted()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InitialProgressState())

//    private val _progress = dao.getKanjiWithAttemptStatus()
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val progress = Pager(
        config = PagingConfig(pageSize = 50, prefetchDistance = 50),
        pagingSourceFactory = { dao.getKanjiWithAttemptStatus() }
    ).flow.cachedIn(viewModelScope)
    val progressState = combine(_progressState, _attempts) {
        state, attempts ->
        state.copy(
            kanjiProgressState = attempts
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), KanjiProgressState())
}