package com.dev.hanji.screens.packs

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dev.hanji.database.AppDatabase
import com.dev.hanji.kanjiPack.KanjiPackDao
import com.dev.hanji.kanjiPack.KanjiPackFactory
import com.dev.hanji.kanjiPack.KanjiPackViewModel

@Composable
fun KanjiPackDetailScreen(modifier: Modifier = Modifier, packId: Long?) {
    val kanjiPackDao: KanjiPackDao = AppDatabase.getInstance(context = LocalContext.current).kanjiPackDao
    val viewModel: KanjiPackViewModel = viewModel(factory = KanjiPackFactory(kanjiPackDao, packId!!))
    val state by viewModel.packDetailState.collectAsStateWithLifecycle()

    Column {
        LazyColumn {
            state.kanjiPackWithKanjiList?.let { kanjiPackWithKanji ->
                item {
                }
                items(kanjiPackWithKanji.kanjiList) { kanji ->
                    Text(text = kanji.character)
                }
            }
        }


    }

}