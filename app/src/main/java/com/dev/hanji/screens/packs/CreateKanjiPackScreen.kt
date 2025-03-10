package com.dev.hanji.screens.packs

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.hanji.kanji.KanjiEntity
import com.dev.hanji.kanjiPack.CreateKanjiPackState
import com.dev.hanji.kanjiPack.KanjiPackEvent
@Composable
fun CreateKanjiPackScreen(modifier: Modifier = Modifier, onEvent: (KanjiPackEvent) -> Unit, state: CreateKanjiPackState) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.width(100.dp),
                onClick = {
                    Log.d("KANJI STATE", "$state")
                    onEvent(KanjiPackEvent.SaveKanjiPack)
                },
            )
            {
                Text("Save")
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ){ _ ->
        Column(modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                label = {
                    Text("Name")
                },
                value = state.name,
                onValueChange = {
                    onEvent(KanjiPackEvent.SetKanjiPackName(it))
                },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                label = {
                    Text("Description")
                },
                value = state.description,
                onValueChange = {
                    onEvent(KanjiPackEvent.SetKanjiDescription(it))
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TabRow(selectedTabIndex = selectedTabIndex, modifier = Modifier.fillMaxWidth()) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text("Available") }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("Selected") }
                )
            }

            if (selectedTabIndex == 0) {
                LazyColumn(modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)) {
                    items(state.availableKanjiList) { kanji ->
                        val isChecked = state.selectedKanjiList.contains(kanji)
                        KanjiItem(kanji = kanji, isChecked = isChecked, onEvent = onEvent)
                    }
                }
            }

            // Content for Selected Kanji
            if (selectedTabIndex == 1) {
                LazyColumn(modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)) {
                    items(state.selectedKanjiList) { kanji ->
                        val isChecked = state.selectedKanjiList.contains(kanji)
                        KanjiItem(kanji = kanji, isChecked = isChecked, onEvent = onEvent)
                    }
                }
            }
        }
    }
}

@Composable
private fun KanjiItem(modifier: Modifier = Modifier, kanji: KanjiEntity, isChecked: Boolean, onEvent: (KanjiPackEvent) -> Unit) {
    HorizontalDivider()
    ListItem(
        headlineContent = {
            Text(kanji.meanings.joinToString(", "), fontSize = 18.sp)
        },
        supportingContent = {
            Column {
                Text("On: ${kanji.readingsOn.joinToString(", ")}", fontSize = 18.sp)
                Text("Kun: ${kanji.readingsKun.joinToString(", ")}", fontSize = 18.sp)
            }
        },
        leadingContent = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(kanji.character, textAlign = TextAlign.Center, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            }
        },
        trailingContent = {
            IconButton(
                onClick = {
                    if(!isChecked) {
                        onEvent(KanjiPackEvent.AddKanjiToPack(kanji))
                    } else {
                        onEvent(KanjiPackEvent.RemoveKanjiFromPack(kanji))
                    }
                }
            ) {
                Icon(imageVector =  if(isChecked) Icons.Filled.Delete else Icons.Filled.Add, contentDescription = "Add Kanji")
            }
        }
    )
}
