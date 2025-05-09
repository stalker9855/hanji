package com.dev.hanji.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dev.hanji.data.dao.DailyAttemptDao
import com.dev.hanji.data.viewmodel.DailyAttemptViewModel

class DailyAttemptFactory(private val dailyAttemptDao: DailyAttemptDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if(modelClass.isAssignableFrom(DailyAttemptViewModel::class.java)) {
                DailyAttemptViewModel(dailyAttemptDao) as T
            } else {
                throw IllegalArgumentException("Unknown model")
            }
        }

    }