package com.dev.hanji.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.hanji.data.DataStoreRepository
import kotlinx.coroutines.launch

class OnBoardViewModel(private val repo: DataStoreRepository) : ViewModel() {
    val readOnBoardingState = repo.readOnBoardingState()

    fun setOnBoardingCompleted() {
        viewModelScope.launch {
            repo.saveOnBoardingState(true)
        }
    }
}