package com.dev.hanji.ui.screens.packs

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import com.dev.hanji.components.CreateEditKanjiItem
import com.dev.hanji.data.model.KanjiEntity
import com.dev.hanji.data.state.CreateEditKanjiPackState
import com.dev.hanji.data.events.KanjiPackEvent


@Composable
fun EditKanjiPackScreen(modifier: Modifier = Modifier,
                        onEvent: (KanjiPackEvent) -> Unit,
                        state: CreateEditKanjiPackState,
                        pagedKanjiList: LazyPagingItems<KanjiEntity>,
                        navController: NavController
) {

    var name by remember { mutableStateOf(state.name) }


    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.width(80.dp),
                onClick = {
                    onEvent(KanjiPackEvent.UpdateKanjiPack)
                    if(state.name.isNotBlank() && state.description.isNotBlank() && state.selectedKanjiList.isNotEmpty()) {
                        navController.popBackStack()
                    }
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
                    Text("Title (1 Character)")
                },
                value = state.title,
                onValueChange = {
                    newValue -> onEvent(KanjiPackEvent.SetTitle(newValue))
                },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                label = {
                    Text("Description")
                },
                value = state.description,
                onValueChange = {
                    newValue -> onEvent(KanjiPackEvent.SetKanjiDescription(newValue))
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                label = { Text("Search") },
                value = state.searchQuery,
                onValueChange = {
                        query -> onEvent(KanjiPackEvent.SetSearchQuery(query))
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
                    items(pagedKanjiList.itemCount) { index ->
                        val kanji = pagedKanjiList[index]
                        if (kanji != null) {
                            val isChecked = state.selectedKanjiList.contains(kanji)
                            CreateEditKanjiItem(kanji = kanji, isChecked = isChecked, onEvent = onEvent)
                        }
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
                        CreateEditKanjiItem(kanji = kanji, isChecked = isChecked, onEvent = onEvent)
                    }
                }
            }
        }
    }
}