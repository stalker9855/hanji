package com.dev.hanji.kanjiPack

import com.dev.hanji.kanji.KanjiEntity

sealed interface KanjiPackEvent {
    data object SaveKanjiPack: KanjiPackEvent
    data class SetKanjiPackName(val name: String): KanjiPackEvent
    data class SetKanjiDescription(val description: String): KanjiPackEvent
    data class SetAvailableKanji(val kanjiList: List<KanjiEntity>): KanjiPackEvent
    data class SetSearchQuery(val query: String): KanjiPackEvent
    data class AddKanjiToPack(val kanji: KanjiEntity): KanjiPackEvent
    data class RemoveKanjiFromPack(val kanji: KanjiEntity):  KanjiPackEvent
    data class DeleteKanjiPack(val kanjiPack: KanjiPackEntity): KanjiPackEvent
}