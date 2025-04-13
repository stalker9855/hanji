package com.dev.hanji.data.events

import com.dev.hanji.data.model.KanjiPackEntity
import com.dev.hanji.data.model.KanjiEntity

sealed interface KanjiPackEvent {
    data object SaveKanjiPack: KanjiPackEvent
    data object UpdateKanjiPack: KanjiPackEvent
    data class SetKanjiPackName(val name: String): KanjiPackEvent
    data class SetKanjiDescription(val description: String): KanjiPackEvent
    data class SetTitle(val title: String): KanjiPackEvent
    data class SetAvailableKanji(val kanjiList: List<KanjiEntity>): KanjiPackEvent
    data class SetSearchQuery(val query: String): KanjiPackEvent
    data class AddKanjiToPack(val kanji: KanjiEntity): KanjiPackEvent
    data class RemoveKanjiFromPack(val kanji: KanjiEntity): KanjiPackEvent
    data class DeleteKanjiPack(val kanjiPack: KanjiPackEntity): KanjiPackEvent
}