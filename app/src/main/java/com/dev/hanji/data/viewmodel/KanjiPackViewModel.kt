package com.dev.hanji.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.dev.hanji.components.SnackbarController
import com.dev.hanji.components.SnackbarEvent
import com.dev.hanji.data.dao.KanjiPackDao
import com.dev.hanji.data.events.KanjiEvent
import com.dev.hanji.data.events.KanjiPackEvent
import com.dev.hanji.data.model.KanjiPackCrossRef
import com.dev.hanji.data.model.KanjiPackEntity
import com.dev.hanji.data.model.KanjiEntity
import com.dev.hanji.data.state.CreateEditKanjiPackState
import com.dev.hanji.data.state.KanjiPackState
import com.dev.hanji.data.state.KanjiPackStateById
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

//    val editKanjiPackState = combine(_createEditKanjiPackState, _kanjiPackWithKanjiListById!!) {
//        state, kanjiListById ->
////        Log.d("ViewModel", "Combining states. Current name: ${state.name}, New name: ${kanjiListById.pack.name}")
//        state.copy(
//            packId = packId,
//            selectedKanjiList =  kanjiListById.kanjiList,
//            name = kanjiListById.pack.name ,
//            description = kanjiListById.pack.description
//        )
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CreateEditKanjiPackState())

    val editKanjiPackState = _createEditKanjiPackState.
        stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CreateEditKanjiPackState())
//        it.copy(
//            packId = packId,
//            name = packData.pack.name,
//            description = packData.pack.description,
//            selectedKanjiList = packData.kanjiList



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
               val title = createKanjiPackState.value.title
               val description = createKanjiPackState.value.description
               val selectedKanjiList = createKanjiPackState.value.selectedKanjiList

               if(!isSingleSymbol(title)) {
                   viewModelScope.launch {
                       SnackbarController.sendEvent(
                           event = SnackbarEvent(
                               message = "Title must be 1 character",
                           )
                       )
                   }
                   return
               }

               if(name.length > 20) {
                   viewModelScope.launch {
                       SnackbarController.sendEvent(
                           event = SnackbarEvent(
                               message = "Name must no be more than 20 symbols",
                           )
                       )
                   }
                   return
               }

               if(name.isBlank() || description.isBlank() || selectedKanjiList.isEmpty() || title.isEmpty()) {
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
                   title = title,
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
               Log.d("ViewModel", "Event received: ${_createEditKanjiPackState.value}")
               _createEditKanjiPackState.update { state ->
                   state.copy(
                       name = event.name
                   )
               }
           }
           is KanjiPackEvent.SetSearchQuery -> {
               _searchQuery.value = event.query
               _createEditKanjiPackState.update { it.copy(searchQuery = event.query) }
           }

           is KanjiPackEvent.SetTitle -> {
               _createEditKanjiPackState.update { state ->
                   state.copy(
                       title = event.title
                   )
               }
           }
       }
    }


    private fun isSingleSymbol(input: String): Boolean {
        return input.trim().codePointCount(0, input.trim().length) == 1
    }
}