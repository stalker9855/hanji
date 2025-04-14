package com.dev.hanji.data.state

data class InitialProgressState(
    val total: Int = 0,
    val attempted: Int = 0,
)

data class KanjiProgressState(
    val kanjiProgressState: InitialProgressState? = null
)
