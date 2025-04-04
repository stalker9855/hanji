package com.dev.hanji.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.dev.hanji.data.dao.KanjiDao
import com.dev.hanji.data.events.KanjiEvent
import com.dev.hanji.data.state.KanjiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest

class KanjiViewModel(private val dao : KanjiDao) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val kanjiList = _searchQuery
        .debounce(500)
        .flatMapLatest { query ->
            Pager(
                config = PagingConfig(
                    pageSize = 5,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = { dao.getKanjiWithPagination(query) }
            ).flow.cachedIn(viewModelScope)
        }



    fun onEvent(event: KanjiEvent) {
        when(event) {
            is KanjiEvent.SetSearchQuery -> {
                _searchQuery.value = event.query
            }
        }
    }


}