package com.dev.hanji.data.events

sealed interface KanjiAttemptEvent {
    data object SaveAttemptKanji: KanjiAttemptEvent
    data class SetCharacter(val character: String): KanjiAttemptEvent
    data class SetUser(val userId: Long): KanjiAttemptEvent
    data class SetCurrentIndex(val index: Int): KanjiAttemptEvent
    data object IncrementError: KanjiAttemptEvent
}