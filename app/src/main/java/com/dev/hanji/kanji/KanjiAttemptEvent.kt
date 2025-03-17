package com.dev.hanji.kanji

sealed interface KanjiAttemptEvent {
    data object SaveAttemptKanji: KanjiAttemptEvent
    data class SetCharacter(val character: String): KanjiAttemptEvent
    data class SetUser(val userId: Int): KanjiAttemptEvent
    data class SetCurrentIndex(val index: Int): KanjiAttemptEvent
}