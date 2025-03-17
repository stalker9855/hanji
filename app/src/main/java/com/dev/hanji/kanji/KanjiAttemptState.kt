package com.dev.hanji.kanji

data class KanjiAttemptState (
    val character: String = "",
    val userId: Int = 1,
    val attempts: Int = 0,
    val eFactor: Double = 2.5,
    val interval: Int = 1,
    val currentIndex: Int = 0,
    val lastReview: Long = System.currentTimeMillis(),
    val nextReviewDate: Long = System.currentTimeMillis() + 24 * 60 * 60 * 1000,
)