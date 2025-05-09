package com.dev.hanji.data.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.hanji.data.dao.DailyAttemptDao
import com.dev.hanji.data.events.DailyEvent
import com.dev.hanji.data.model.DailyAttempt
import com.dev.hanji.data.state.DailyState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DailyAttemptViewModel(private val dao: DailyAttemptDao) : ViewModel() {

    private val _dailyState = MutableStateFlow(DailyState())

    private val _dailyAttempts = dao.getDailyAttempts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val dailyState = combine(_dailyState, _dailyAttempts) {
        state, attempts ->
            state.copy(
                dailyAttempts = attempts,
            )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), DailyState())


    fun onEvent(event: DailyEvent) {
        when(event) {
            is DailyEvent.GetDate ->
            {
                ensureUpdate()
            }
            DailyEvent.IncrenemtAttempt -> {
                ensureUpdate { it.copy(attempts = it.attempts + 1) }
            }
            DailyEvent.IncrenemtAttemptLine -> {
                ensureUpdate { it.copy(attemptsLines = it.attemptsLines + 1) }
            }
        }
    }

    private fun ensureUpdate(updateValue: ((DailyAttempt) -> DailyAttempt)? = null) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val today = getTodayDate()
                val existing = dao.getByDate(today)
                if (existing == null) {
                    dao.insert(DailyAttempt(
                        date = today,
                        userId = 1))
                } else if (updateValue != null) {
                    dao.update(updateValue(existing))
                }

            }

        }

    }

    private fun getTodayDate(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            .withZone(ZoneId.systemDefault())
        return formatter.format(Instant.now())
    }
}

fun formatDisplayDate(isoDate: String): String {
    val formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formatterOutput = DateTimeFormatter.ofPattern("MM/dd")
    val date = LocalDate.parse(isoDate, formatterInput)
    return formatterOutput.format(date)
}
