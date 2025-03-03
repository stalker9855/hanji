package com.dev.hanji.kanjiPack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.hanji.kanji.KanjiEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KanjiPackViewModel(private val dao: KanjiPackDao) : ViewModel() {

    private val _state = MutableStateFlow(KanjiPackState())
    private val _kanjiPacks = dao.getAllPacks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _kanjiList = dao.getAllPacksWithKanji()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())


    val state = combine(_state, _kanjiPacks, _kanjiList) {
        state, kanjiPacks, kanjiList ->
            state.copy(
                kanjiList = kanjiList.flatMap { it.kanjiList },
                kanjiPacks = kanjiPacks,
            )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), KanjiPackState())


    fun onEvent(event: KanjiPackEvent) {
       when (event) {
           is KanjiPackEvent.DeleteKanjiPack -> {
               viewModelScope.launch {
                   dao.deletePack(event.kanjiPack)
               }
           }
           KanjiPackEvent.SaveKanjiPack -> {
               val name = state.value.name
               val description = state.value.description
               val kanjiList = state.value.kanjiList

               if(name.isBlank() || description.isBlank() || kanjiList.isEmpty()) {
                   return
               }

               val kanjiPack = KanjiPackEntity(
                   name = name,
                   description = description,
                   userId = 1,
               )
               viewModelScope.launch {
                   val packId = dao.upsertPack(kanjiPack)
                   val crossRefs = kanjiList.map { kanjiEntity: KanjiEntity ->
                       KanjiPackCrossRef(
                           packId = packId.toInt(),
                           character = kanjiEntity.character
                       )
                   }
                   dao.insertKanjiPackCrossRef(crossRefs)
               }
           }
           is KanjiPackEvent.SetKanjiCharacters -> {
               _state.update { it.copy(
                   kanjiList = event.kanjiList
               ) }
           }
           is KanjiPackEvent.SetKanjiDescription -> {
               _state.update { it.copy(
                   description = event.description
               ) }
           }
           is KanjiPackEvent.SetKanjiPackName -> {
               _state.update {
                   it.copy(
                       name = event.name
                   )
               }
           }
       }
    }
}