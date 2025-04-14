package com.dev.hanji.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dev.hanji.data.dao.KanjiDao
import com.dev.hanji.data.viewmodel.KanjiViewModel

class KanjiFactory(private val kanjiDao: KanjiDao, private val character: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if(modelClass.isAssignableFrom(KanjiViewModel::class.java)) {
                KanjiViewModel(kanjiDao, character) as T
            } else {
                throw IllegalArgumentException("Unknown model")
            }
        }

    }