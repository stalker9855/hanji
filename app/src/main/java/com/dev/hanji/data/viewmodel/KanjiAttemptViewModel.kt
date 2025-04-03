package com.dev.hanji.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.hanji.data.dao.KanjiAttemptDao
import com.dev.hanji.data.events.KanjiAttemptEvent
import com.dev.hanji.data.model.KanjiAttemptEntity
import com.dev.hanji.data.state.KanjiAttemptState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class KanjiAttemptViewModel(private val dao: KanjiAttemptDao) : ViewModel() {

    private val _state = MutableStateFlow(KanjiAttemptState())


    val state =
        _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), KanjiAttemptState())


    fun onEvent(event: KanjiAttemptEvent) {
        when (event) {
            KanjiAttemptEvent.SaveAttemptKanji -> {
                viewModelScope.launch {
                    state.value.attemptsList.forEach { kanjiAttempt ->
                        dao.insert(kanjiAttempt)
                    }
                }
            }

            is KanjiAttemptEvent.SetCharacter -> {
                val existingAttempt =
                    _state.value.attemptsList.find { it.character == event.character }
                if (existingAttempt == null) {
                    val newAttempt = KanjiAttemptEntity(
                        character = event.character,
                        userId = _state.value.userId,
                        attempts = 0,
                        errors = 0,
                    )
                    _state.update {
                        it.copy(
                            attemptsList = it.attemptsList + newAttempt
                        )
                    }
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

            KanjiAttemptEvent.IncrementError -> {
                _state.update { state ->
                    val updatedList = state.attemptsList.map {
                        if (it.character == state.attemptsList[state.currentIndex].character) {
                            it.copy(errors = it.errors + 1)
                        } else it
                    }
                    state.copy(attemptsList = updatedList)
                }
                Log.d("UPDADED", "${_state.value}")
            }
        }

    }
}

