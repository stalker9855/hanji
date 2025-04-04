package com.dev.hanji.data.events

sealed interface KanjiEvent {
    data class SetSearchQuery(val query: String): KanjiEvent
}