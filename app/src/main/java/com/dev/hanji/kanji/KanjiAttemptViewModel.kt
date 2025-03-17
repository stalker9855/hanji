package com.dev.hanji.kanji

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class KanjiAttemptViewModel(private val dao: KanjiAttemptDao) : ViewModel() {

    private val _state = MutableStateFlow(KanjiAttemptState())


    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), KanjiAttemptState())


    fun onEvent(event: KanjiAttemptEvent)  {
        when (event) {
            KanjiAttemptEvent.SaveAttemptKanji -> {
                val character = _state.value.character
                val userId = _state.value.userId
                val attempts = _state.value.attempts
                val kanjiAttempt = KanjiAttemptEntity(
                    character = character,
                    userId = userId,
                    attempts = attempts
                )
                viewModelScope.launch {
                    dao.insert(kanjiAttempt)
                }
            }
            is KanjiAttemptEvent.SetCharacter -> {
                _state.update {
                    it.copy(
                        character = event.character
                    )
                }
            }
            is KanjiAttemptEvent.SetCurrentIndex -> {
                _state.update {
                    it.copy(
                        currentIndex = event.index
                    )
                }
            }
            is KanjiAttemptEvent.SetUser -> {
                _state.update {
                    it.copy(
                        userId = event.userId
                    )
                }
            }
        }
    }



}