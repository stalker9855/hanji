package com.dev.hanji.ui.screens.packs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dev.hanji.Draw
import com.dev.hanji.EditPack
import com.dev.hanji.components.KanjiItem
import com.dev.hanji.components.SnackbarController
import com.dev.hanji.components.SnackbarEvent
import com.dev.hanji.data.model.KanjiEntity
import com.dev.hanji.data.model.KanjiPackEntity
import com.dev.hanji.data.events.KanjiPackEvent
import com.dev.hanji.data.state.KanjiPackStateById
import kotlinx.coroutines.launch

@Composable
fun KanjiPackDetailScreen(
    modifier: Modifier = Modifier,
    state: KanjiPackStateById,
    onEvent: (KanjiPackEvent) -> Unit,
    navController: NavController) {

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showPracticeDialog by remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)},
        floatingActionButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FloatingActionButton(onClick = {
                    // can i a pass a state? not id, but object
                    navController.navigate("${EditPack.route}/${state.packId}")
                }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit kanji pack")
                }
                FloatingActionButton(onClick = {
                    // dialog
                    showDeleteDialog = true
                }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete kanji pack")
                }
                FloatingActionButton(onClick = {
                    showPracticeDialog = true

                }) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = "Practice")
                }

            }
        }
    ) { _ ->
        Column {
            LazyColumn {
                state.kanjiPackWithKanjiList?.let { kanjiPackWithKanji ->
                    item {
                        PackDetail(kanjiPack = kanjiPackWithKanji.pack, count = kanjiPackWithKanji.kanjiCount)
                    }
                    items(kanjiPackWithKanji.kanjiList) { kanji ->
                        KanjiItem(kanji = kanji)
                    }
                }
            }
        }

    }

    if(showPracticeDialog) {
        AlertDialog(
            onDismissRequest = { showPracticeDialog = false },
            title = { Text("Practice") },
            text = { Text("Want to start this pack?") },
            confirmButton = {
                Button(
                    onClick = {
                        showPracticeDialog = false
                        navController.navigate("${Draw.route}/${state.packId}")
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showPracticeDialog = false }
                ) {
                    Text("No")
                }
            }
        )
    }


    if(showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Warning!") },
            text = { Text("Are you sure to delete this pack?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        scope.launch {
                            onEvent(KanjiPackEvent.DeleteKanjiPack(state.kanjiPackWithKanjiList!!.pack))
                            navController.popBackStack()
                            SnackbarController.sendEvent(
                                SnackbarEvent(
                                    message = "Kanji pack deleted"
                                )
                            )
                        }
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("No")
                }
            }
        )
    }
}




@Composable
fun PackDetail(modifier: Modifier = Modifier, kanjiPack: KanjiPackEntity, count: Int) {
    val checked = remember { mutableStateOf(false) } // temporary value
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .shadow(4.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .size(96.dp)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(kanjiPack.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 48.sp
            )
        }
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(kanjiPack.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider(modifier = Modifier.padding(8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column {
                        Text(kanjiPack.description)
                        Text("Kanji Count: $count")
                    }
                    IconToggleButton(
                        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer),
                        checked = checked.value,
                        onCheckedChange = { checked.value = it }
                    ) {
                        Icon(imageVector = Icons.Filled.Favorite, contentDescription = "")

                    }
                }
            }
        }
    }
}
