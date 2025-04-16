package com.dev.hanji.ui.screens.kanji

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import com.dev.hanji.KanjiDetail
import com.dev.hanji.components.KanjiItem
import com.dev.hanji.data.events.KanjiEvent
import com.dev.hanji.data.model.KanjiEntity

@Composable
fun KanjiAllScreen(modifier: Modifier = Modifier,
                   kanjiList: LazyPagingItems<KanjiEntity>,
                   navController: NavController,
                   onEvent: (KanjiEvent) -> Unit) {
   var query by remember { mutableStateOf("") }
   Column {
      OutlinedTextField(
         label = { Text(text = "Search") },
         value = query,
         onValueChange = {
               newText ->
            query = newText
            onEvent(KanjiEvent.SetSearchQuery(newText))
         },
         modifier = Modifier.fillMaxWidth()
      )
      LazyColumn(modifier = modifier) {
         items(kanjiList.itemCount) { index ->
            val kanji = kanjiList[index]
            if (kanji != null) {
               KanjiItem(kanji = kanji, modifier = Modifier
                  .clickable {
                     navController.navigate("${KanjiDetail.route}/${kanji.character}")
                  }
               )
            }
         }
      }

   }
}