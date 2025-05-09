package com.dev.hanji.data.events

sealed interface DailyEvent {
    data object IncrenemtAttempt: DailyEvent
    data object IncrenemtAttemptLine: DailyEvent
    data object GetDate: DailyEvent
}