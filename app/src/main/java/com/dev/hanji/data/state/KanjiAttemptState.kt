package com.dev.hanji.data.state

import com.dev.hanji.data.model.KanjiAttemptEntity

data class KanjiAttemptState (
    val attemptsList: List<KanjiAttemptEntity> = emptyList(),
    val character: String = "",
    val userId: Long = 1,
    val attempts: Long = 0,
    val errors: Long = 0,
    val eFactor: Double = 2.5,
    val interval: Int = 1,
    val currentIndex: Int = 0,
    val lastReview: Long = System.currentTimeMillis(),
    val nextReviewDate: Long = System.currentTimeMillis() + 24 * 60 * 60 * 1000,
)