package com.dev.hanji.kanjiPack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KanjiPackViewModel(val dao: KanjiPackDao) : ViewModel() {

    private val _state = MutableStateFlow(KanjiPackState())

    private val _kanjiPacks = MutableStateFlow<List<PackWithKanji>>(emptyList())
    val kanjiPacks: StateFlow<List<PackWithKanji>> = _kanjiPacks

    val state = combine(_state, _kanjiPacks) {
        state, kanjiPacks ->
            state.copy(
                kanji = kanjiPacks.flatMap { it.kanjiList },
            )
    }
    init {
        viewModelScope.launch {
            val kanjiPacksList = withContext(Dispatchers.IO) {
                dao.getAllPacksWithKanji()
            }
            _kanjiPacks.value = kanjiPacksList
        }
    }


    fun onEvent(event: KanjiPackEvent) {
       when (event) {
           is KanjiPackEvent.DeleteKanjiPack -> {
               viewModelScope.launch {
                   dao.deletePack(event.kanjiPack)
               }
           }
           KanjiPackEvent.SaveKanjiPack -> {
               val name = _state.value.name
               val description = _state.value.description
               val kanji = _state.value.kanji

               if(name.isBlank() || description.isBlank() || kanji.isEmpty()) {
                   return
               }

               val kanjiPack = KanjiPackEntity(
                   name = name,
                   description = description,
                   userId = 1,
               )
               viewModelScope.launch {
                   val packId = dao.upsertPack(kanjiPack)
                   val crossRefs = kanji.map { kanjiEntity ->
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
                   kanji = event.kanji
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