package com.dev.hanji.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dev.hanji.data.dao.KanjiPackDao
import com.dev.hanji.data.viewmodel.KanjiPackViewModel

class KanjiPackFactory(private val kanjiPackDao: KanjiPackDao, private val packId: Long) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(KanjiPackViewModel::class.java)) {
            KanjiPackViewModel(kanjiPackDao, packId) as T
        } else {
            throw IllegalArgumentException("Unknown model")
        }
    }

}
