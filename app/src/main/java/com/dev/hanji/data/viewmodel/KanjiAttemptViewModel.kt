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
                            updateKanjiAttempt(it, userGrade = 2)
                        } else it
                    }
                    state.copy(attemptsList = updatedList)
                }
            }
        }
    }

    private fun updateKanjiAttempt(attempt: KanjiAttemptEntity, userGrade: Int): KanjiAttemptEntity {
        val now = System.currentTimeMillis()
        val isEarlyReview = now < attempt.nextReviewDate


        val newErrors = attempt.errors + 1
        val newEFactor = calculateEFactor(attempt.eFactor, userGrade)

        val newInterval = when {
            newErrors > 3 -> 1
            isEarlyReview -> attempt.interval
            else -> calculateInterval(attempt.interval, newEFactor)
        }


        val nextReview = System.currentTimeMillis() + newInterval * 24 * 60 * 60 * 1000

        return attempt.copy(
            errors = newErrors,
            eFactor = newEFactor,
            interval = newInterval,
            lastReview = System.currentTimeMillis(),
            nextReviewDate = nextReview
        )
    }

    private fun calculateEFactor(eFactor: Double, grade: Int): Double {
        val newEFactor = eFactor + (0.1 - (5 - grade) * (0.08 + (5 - grade) * 0.02))
        return newEFactor.coerceIn(1.3, 2.5)
    }

    private fun calculateInterval(previousInterval: Int, eFactor: Double): Int {
        return if (previousInterval == 1) 2 else (previousInterval * eFactor).toInt()
    }
}

