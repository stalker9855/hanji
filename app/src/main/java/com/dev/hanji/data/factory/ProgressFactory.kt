package com.dev.hanji.data.factory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dev.hanji.data.dao.ProgressDao
import com.dev.hanji.data.viewmodel.ProgressViewModel

class ProgressFactory(private val progressDao: ProgressDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(ProgressViewModel::class.java)) {
            ProgressViewModel(progressDao) as T
        } else {
            throw IllegalArgumentException("Unknown model")
        }
    }
}
