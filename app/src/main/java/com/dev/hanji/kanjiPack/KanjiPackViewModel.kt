package com.dev.hanji.kanjiPack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.dev.hanji.kanji.KanjiEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class KanjiPackViewModel(private val dao: KanjiPackDao, packId: Long? = 6) : ViewModel() {

    // private states
    private val _state = MutableStateFlow(KanjiPackState())
    private val _kanjiPackDetailState = MutableStateFlow(KanjiPackStateById())
    private val _createKanjiPackState = MutableStateFlow(CreateKanjiPackState())




    // queries
    private val _kanjiPacks = dao.getAllPacks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _kanjiPackWithKanjiListById = packId?.let { dao.getKanjiListByPackId(packId = it) }

//    private val _kanjiList = dao.getAllKanji()

    private val _searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val pagedKanjiList = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { dao.getKanjiWithPagination(query) }
        ).flow.cachedIn(viewModelScope)
    }

    // public states
    val state = combine(_state, _kanjiPacks) {
        state, kanjiPacks  ->
            state.copy(
                kanjiPacks = kanjiPacks,
            )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), KanjiPackState())

    val packDetailState = combine(_kanjiPackDetailState,  _kanjiPackWithKanjiListById!!) {
       state, kanjiListById ->
        state.copy(
            kanjiPackWithKanjiList = kanjiListById
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), KanjiPackStateById())

    val createKanjiPackState = _createKanjiPackState.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CreateKanjiPackState())



    fun onEvent(event: KanjiPackEvent) {
       when (event) {
           is KanjiPackEvent.DeleteKanjiPack -> {
               viewModelScope.launch {
                   dao.deletePack(event.kanjiPack)
               }
           }
           KanjiPackEvent.SaveKanjiPack -> {
               val name = createKanjiPackState.value.name
               val description = createKanjiPackState.value.description
               val selectedKanjiList = createKanjiPackState.value.selectedKanjiList

               if(name.isBlank() || description.isBlank() || selectedKanjiList.isEmpty()) {
                   return
               }

               val kanjiPack = KanjiPackEntity(
                   name = name,
                   description = description,
                   userId = 1,
               )
               viewModelScope.launch {
                   val packId = dao.upsertPack(kanjiPack)
                   val crossRefs = selectedKanjiList.map { kanjiEntity: KanjiEntity ->
                       KanjiPackCrossRef(
                           packId = packId.toInt(),
                           character = kanjiEntity.character
                       )
                   }
                   dao.insertKanjiPackCrossRef(crossRefs)
               }
           }
           is KanjiPackEvent.SetAvailableKanji -> {
               _createKanjiPackState.update { it.copy(availableKanjiList = event.kanjiList) }
           }
           is KanjiPackEvent.AddKanjiToPack -> {
               _createKanjiPackState.update { it.copy(
                   selectedKanjiList = it.selectedKanjiList + event.kanji
               ) }
           }
           is KanjiPackEvent.RemoveKanjiFromPack -> {
               _createKanjiPackState.update { it.copy(
                   selectedKanjiList = it.selectedKanjiList - event.kanji
               ) }
           }
           is KanjiPackEvent.SetKanjiDescription -> {
               _createKanjiPackState.update { it.copy(
                   description = event.description
               ) }
           }
           is KanjiPackEvent.SetKanjiPackName -> {
               _createKanjiPackState.update {
                   it.copy(
                       name = event.name
                   )
               }
           }
           is KanjiPackEvent.SetSearchQuery -> {
               _searchQuery.value = event.query
               _createKanjiPackState.update { it.copy(searchQuery = event.query) }
           }
       }
    }
}