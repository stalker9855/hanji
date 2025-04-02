package com.dev.hanji.kanjiPack

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.dev.hanji.components.SnackbarController
import com.dev.hanji.components.SnackbarEvent
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

class KanjiPackViewModel(private val dao: KanjiPackDao, packId: Long? = 0) : ViewModel() {

    // private states
    private val _state = MutableStateFlow(KanjiPackState())
    private val _kanjiPackDetailState = MutableStateFlow(KanjiPackStateById())
    private val _createEditKanjiPackState = MutableStateFlow(CreateEditKanjiPackState())





    // queries
    private val _kanjiPacks = dao.getAllPacks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _kanjiPackWithKanjiListById = packId?.let { dao.getKanjiListByPackId(packId = it) }

//    private val _kanjiList = dao.getAllKanji()

    private val _searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val pagedKanjiList = _searchQuery
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
            packId = packId,
            kanjiPackWithKanjiList = kanjiListById
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), KanjiPackStateById())

    val createKanjiPackState = _createEditKanjiPackState.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CreateEditKanjiPackState())

    val editKanjiPackState = combine(_createEditKanjiPackState, _kanjiPackWithKanjiListById!!) {
        state, kanjiListById ->
        Log.d("ViewModel", "Combining states. Current name: ${state.name}, New name: ${kanjiListById.pack.name}")
        state.copy(
            packId = packId,
            selectedKanjiList =  kanjiListById.kanjiList ,
            name = kanjiListById.pack.name ,
            description = kanjiListById.pack.description
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CreateEditKanjiPackState())



    fun onEvent(event: KanjiPackEvent) {
       when (event) {
           is KanjiPackEvent.DeleteKanjiPack -> {
               viewModelScope.launch {
                   dao.deletePack(event.kanjiPack)
               }
           }
           is KanjiPackEvent.UpdateKanjiPack -> {
               val id = editKanjiPackState.value.packId
               val name = editKanjiPackState.value.name
               val description = editKanjiPackState.value.description
               val selectedKanjiList = editKanjiPackState.value.selectedKanjiList

               if(name.isBlank() || description.isBlank() || selectedKanjiList.isEmpty()) {
                   viewModelScope.launch {
                       SnackbarController.sendEvent(
                           event = SnackbarEvent(
                               message = "Name, Description or Kanji must be not empty",
                           )
                       )
                   }
                   return
               }

               val kanjiPack = KanjiPackEntity(
                   id = id!!,
                   name = name,
                   description = description,
                   userId = 1,
               )
               viewModelScope.launch {
                   dao.upsertPack(kanjiPack)
                   val crossRefs = selectedKanjiList.map { kanjiEntity: KanjiEntity ->
                       KanjiPackCrossRef(
                           packId = id,
                           character = kanjiEntity.character
                       )
                   }
                   dao.upsertKanjiPackCrossRef(crossRefs)
                   SnackbarController.sendEvent(
                       event = SnackbarEvent(
                           message = "Kanji pack was created",
                       )
                   )
               }

           }
           is KanjiPackEvent.SaveKanjiPack -> {
               val name = createKanjiPackState.value.name
               val description = createKanjiPackState.value.description
               val selectedKanjiList = createKanjiPackState.value.selectedKanjiList

               if(name.isBlank() || description.isBlank() || selectedKanjiList.isEmpty()) {
                   viewModelScope.launch {
                       SnackbarController.sendEvent(
                           event = SnackbarEvent(
                               message = "Name, Description or Kanji must be not empty",
                           )
                       )
                   }
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
                           packId = packId,
                           character = kanjiEntity.character
                       )
                   }
                   dao.insertKanjiPackCrossRef(crossRefs)
                   SnackbarController.sendEvent(
                       event = SnackbarEvent(
                           message = "Kanji pack was created",
                       )
                   )
               }
           }
           is KanjiPackEvent.SetAvailableKanji -> {
               _createEditKanjiPackState.update { it.copy(availableKanjiList = event.kanjiList) }
           }
           is KanjiPackEvent.AddKanjiToPack -> {
               _createEditKanjiPackState.update { it.copy(
                   selectedKanjiList = it.selectedKanjiList + event.kanji
               ) }
           }
           is KanjiPackEvent.RemoveKanjiFromPack -> {
               _createEditKanjiPackState.update { it.copy(
                   selectedKanjiList = it.selectedKanjiList - event.kanji
               ) }
           }
           is KanjiPackEvent.SetKanjiDescription -> {
               _createEditKanjiPackState.update { it.copy(
                   description = event.description
               ) }
           }
           is KanjiPackEvent.SetKanjiPackName -> {
               Log.d("ViewModel", "Event received: ${event.name}")
               _createEditKanjiPackState.update {
                   it.copy(
                       name = event.name
                   )
               }
           }
           is KanjiPackEvent.SetSearchQuery -> {
               _searchQuery.value = event.query
               _createEditKanjiPackState.update { it.copy(searchQuery = event.query) }
           }
       }
    }
}