package com.dev.hanji.data.state

import com.dev.hanji.data.model.DailyAttempt

data class DailyState(
    val date: String = "",
    val today: String = "",
    val attempts: Long = 0,
    val attemptsLines: Long = 0,
    val dailyAttempts: List<DailyAttempt> = listOf()
)
