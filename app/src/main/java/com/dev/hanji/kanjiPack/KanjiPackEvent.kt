package com.dev.hanji.kanjiPack

import com.dev.hanji.kanji.KanjiEntity

sealed interface KanjiPackEvent {
    object SaveKanjiPack: KanjiPackEvent
    data class SetKanjiPackName(val name: String): KanjiPackEvent
    data class SetKanjiDescription(val description: String): KanjiPackEvent
    data class SetKanjiCharacters(val kanji: List<KanjiEntity>): KanjiPackEvent
    data class DeleteKanjiPack(val kanjiPack: KanjiPackEntity): KanjiPackEvent
}