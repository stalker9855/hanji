package com.dev.hanji.screens.packs

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.hanji.kanjiPack.CreateKanjiPackState
import com.dev.hanji.kanjiPack.KanjiPackEvent
@Composable
fun CreateKanjiPackScreen(modifier: Modifier = Modifier, onEvent: (KanjiPackEvent) -> Unit, state: CreateKanjiPackState) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Box(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            TextField(
                value = state.name,
                onValueChange = {
                    onEvent(KanjiPackEvent.SetKanjiPackName(it))
                },
                placeholder = { Text("Kanji pack name") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = state.description,
                onValueChange = {
                    onEvent(KanjiPackEvent.SetKanjiDescription(it))
                },
                placeholder = { Text("Kanji pack description") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tabs
            TabRow(selectedTabIndex = selectedTabIndex, modifier = Modifier.fillMaxWidth()) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text("Available Kanji") }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("Selected Kanji") }
                )
            }

            if (selectedTabIndex == 0) {
                LazyColumn(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                    items(state.availableKanjiList) { kanji ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(4.dp)
                                .clickable { onEvent(KanjiPackEvent.AddKanjiToPack(kanji)) }
                        ) {
                            Text(kanji.character, fontSize = 24.sp, modifier = Modifier.weight(1f))
                            Text("Add", color = Color.Blue)
                        }
                    }
                }
            }

            // Content for Selected Kanji
            if (selectedTabIndex == 1) {
                LazyColumn(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                    items(state.selectedKanjiList) { kanji ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(4.dp)
                                .clickable { onEvent(KanjiPackEvent.RemoveKanjiFromPack(kanji)) }
                        ) {
                            Text(kanji.character, fontSize = 24.sp, modifier = Modifier.weight(1f))
                            Text("Remove", color = Color.Red)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                Log.d("KANJI STATE", "$state")
                onEvent(KanjiPackEvent.SaveKanjiPack)
            }) {
                Text("Save Pack")
            }
        }
    }
}
