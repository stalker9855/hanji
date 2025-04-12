package com.dev.hanji.data.events

import com.dev.hanji.data.model.KanjiPackEntity

sealed interface KanjiAttemptEvent {
    data object SaveAttemptKanji: KanjiAttemptEvent
    data class SetCharacter(val character: String): KanjiAttemptEvent
    data class SetUser(val userId: Long): KanjiAttemptEvent
    data class SetCurrentIndex(val index: Int): KanjiAttemptEvent
    data object IncrementError: KanjiAttemptEvent
    data object IncrementAttempt: KanjiAttemptEvent
    data object IncrementClean: KanjiAttemptEvent
    data object SaveGeneratedKanjiPack: KanjiAttemptEvent
}