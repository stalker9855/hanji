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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class KanjiViewModel(private val dao : KanjiDao, private val character: String?) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    private val _kanjiCharacter = dao.getKanjiByCharacter(character!!)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _kanjiAttempt = dao.getAttemptKanjiByCharacter(character!!)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _kanjiState = MutableStateFlow(KanjiState())

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

    val kanjiState = combine(_kanjiState, _kanjiCharacter, _kanjiAttempt) {
        state, character, attempt ->
            state.copy(
                character = character,
                attempt = attempt
            )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), KanjiState())






    fun onEvent(event: KanjiEvent) {
        when(event) {
            is KanjiEvent.SetSearchQuery -> {
                _searchQuery.value = event.query
            }
        }
    }


}