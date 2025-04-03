package com.dev.hanji.data.factory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dev.hanji.data.dao.KanjiAttemptDao
import com.dev.hanji.data.viewmodel.KanjiAttemptViewModel

class KanjiAttemptFactory(private val kanjiAttemptDao: KanjiAttemptDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(KanjiAttemptViewModel::class.java)) {
            KanjiAttemptViewModel(kanjiAttemptDao) as T
        } else {
            throw IllegalArgumentException("Unknown model")
        }
    }

}
