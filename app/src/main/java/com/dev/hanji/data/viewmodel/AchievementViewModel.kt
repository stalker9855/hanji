package com.dev.hanji.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.dev.hanji.data.dao.AchievementDao
import com.dev.hanji.data.state.KanjiProgressState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class AchievementViewModel(private val dao: AchievementDao): ViewModel() {
//    private val _progressState = MutableStateFlow(KanjiProgressState())

//    private val _progress = dao.getKanjiWithAttemptStatus()
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val progress = Pager(
        config = PagingConfig(pageSize = 50),
        pagingSourceFactory = { dao.getKanjiWithAttemptStatus() }
    ).flow.cachedIn(viewModelScope)
//    val progressState = combine(_progressState, progress) {
//        state, progress ->
//        state.copy(
//            progress = progress
//        )
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), KanjiProgressState())
}