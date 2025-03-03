package com.dev.hanji.kanjiPack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class KanjiPackFactory(private val kanjiPackDao: KanjiPackDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(KanjiPackViewModel::class.java)) {
            KanjiPackViewModel(kanjiPackDao) as T
        } else {
            throw IllegalArgumentException("Unknown model")
        }
    }

}
