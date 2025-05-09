package com.dev.hanji.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.hanji.components.SnackbarController
import com.dev.hanji.components.SnackbarEvent
import com.dev.hanji.data.dao.KanjiAttemptDao
import com.dev.hanji.data.events.KanjiAttemptEvent
import com.dev.hanji.data.model.KanjiAttemptEntity
import com.dev.hanji.data.model.KanjiPackCrossRef
import com.dev.hanji.data.model.KanjiPackEntity
import com.dev.hanji.data.state.KanjiAttemptState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class KanjiAttemptViewModel(private val dao: KanjiAttemptDao) : ViewModel() {

    private val _practiceState = MutableStateFlow(KanjiAttemptState())
    private val _attemptState = MutableStateFlow(KanjiAttemptState())

    private val _kanjiAttempts = dao.getAllAttemptsKanji()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())



    val practiceState =
        _practiceState.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), KanjiAttemptState())

    val attemptState = combine(_attemptState, _kanjiAttempts) {
        state, kanjiAttempts ->
        state.copy(
            attemptsList = kanjiAttempts
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), KanjiAttemptState())


    fun onEvent(event: KanjiAttemptEvent) {
        when (event) {
            KanjiAttemptEvent.SaveAttemptKanji -> {
                viewModelScope.launch {
                    practiceState.value.attemptsList.forEach { kanjiAttempt ->
                        dao.insert(kanjiAttempt)
                    }
                }
            }

            is KanjiAttemptEvent.SetCharacter -> {
                viewModelScope.launch {
                    val existingAttempt = dao.getKanjiByCharacter(event.character)

                    val updatedAttempt = existingAttempt?.copy(
                        character = event.character
                    ) ?: KanjiAttemptEntity(
                        character = event.character,
                        userId = 1,
                        attempts = 0,
                        errors = 0
                    )

                    if (existingAttempt != null) {
                        dao.update(updatedAttempt)
                    } else {
                        dao.insert(updatedAttempt)
                    }

                    _practiceState.update {
                        it.copy(
                            attemptsList = it.attemptsList + updatedAttempt
                        )
                    }
                }
            }



            is KanjiAttemptEvent.SetCurrentIndex -> {
                _practiceState.update {
                    it.copy(
                        currentIndex = event.index
                    )
                }
            }

            is KanjiAttemptEvent.SetUser -> {
                _practiceState.update {
                    it.copy(
                        userId = event.userId
                    )
                }
            }

            is KanjiAttemptEvent.IncrementError -> {
                viewModelScope.launch {
                    _practiceState.update { state ->
                        val updatedList = state.attemptsList.mapIndexed { index, attempt ->
                            if (index == state.currentIndex) {
                                val updatedAttempt = updateKanjiAttempt(attempt, userGrade = 2)

                                viewModelScope.launch {
                                    dao.update(updatedAttempt)
                                }

                                updatedAttempt
                            } else attempt
                        }
                        state.copy(attemptsList = updatedList)
                    }
                }
            }


            is KanjiAttemptEvent.IncrementAttempt -> {
                _practiceState.update { state ->
                    val updatedList = state.attemptsList.mapIndexed { index, attempt ->
                        if (index == state.currentIndex) {
                            val updatedAttempt = attempt.copy(attempts = attempt.attempts + 1)
                            viewModelScope.launch {
                                dao.update(updatedAttempt)
                            }
                            updatedAttempt
                        } else attempt
                    }
                    state.copy(attemptsList = updatedList)
                }
            }

            is KanjiAttemptEvent.IncrementAttemptOnTheEnd -> {
                _practiceState.update { state ->
                    val updatedList = state.attemptsList.mapIndexed { index, attempt ->
                        if (index == state.currentIndex) {
                            val updatedAttempt = when (event.currentErrors) {
                                0 -> {
                                    attempt.copy(clean = attempt.clean + 1)
                                }
                                in 1..4 -> {
                                    attempt.copy(good = attempt.good + 1)
                                }
                                 in 5..Int.MAX_VALUE -> {
                                    attempt.copy(bad = attempt.bad + 1)
                                }
                                else -> attempt
                            }
                            viewModelScope.launch {
                                dao.update(updatedAttempt)
                            }
                            updatedAttempt
                        } else {
                            attempt
                        }
                    }
                    state.copy(attemptsList = updatedList)
                }
            }

            is KanjiAttemptEvent.SaveGeneratedKanjiPack -> {
                viewModelScope.launch {
                    val attemptsList = dao.getKanjiDueTomorrow(userId = 1).first()

                    val kanjiPack = KanjiPackEntity(
                        name = formatTimestamp(timestamp = System.currentTimeMillis()),
                        description = formatTimestamp(timestamp = System.currentTimeMillis()),
                        userId = 1
                    )
                    val packId = dao.insertKanjiPack(kanjiPack)

                    val crossRefs = attemptsList.map { kanjiEntity ->
                        KanjiPackCrossRef(
                            packId = packId,
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


        val nextReview = now + newInterval * 24 * 60 * 60 * 1000

        return attempt.copy(
            errors = newErrors,
            eFactor = newEFactor,
            interval = newInterval,
            lastReview = now,
            nextReviewDate = nextReview
        )
    }

    private fun calculateEFactor(eFactor: Double, grade: Int): Double {
        val newEFactor = eFactor + (0.1 - (5 - grade) * (0.08 + (5 - grade) * 0.02))
        return newEFactor.coerceIn(1.3, 2.5)
    }

    private fun calculateInterval(previousInterval: Int, eFactor: Double): Int {
        return when (previousInterval) {
            0 -> 1
            1 -> 6
            else -> (previousInterval * eFactor).toInt()
        }
    }


    private fun formatTimestamp(timestamp: Long): String {
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
            .withZone(ZoneId.systemDefault())
        return formatter.format(Instant.ofEpochMilli(timestamp))
    }
}

