package com.dev.hanji.screens.packs

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dev.hanji.database.AppDatabase
import com.dev.hanji.kanjiPack.KanjiPackDao
import com.dev.hanji.kanjiPack.KanjiPackFactory
import com.dev.hanji.kanjiPack.KanjiPackState
import com.dev.hanji.kanjiPack.KanjiPackViewModel


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AllPacksScreen(modifier: Modifier = Modifier,
                   navController: NavController) {
    val kanjiPackDao: KanjiPackDao = AppDatabase.getInstance(context = LocalContext.current).kanjiPackDao
    val viewModel: KanjiPackViewModel = viewModel(factory = KanjiPackFactory(kanjiPackDao))
    val state by viewModel.state.collectAsState(initial = KanjiPackState())
    Log.d("VIEWMODEL", "${state.kanji}")
    Box {
        LazyColumn(
            modifier = modifier
                .padding(top = 8.dp)
//                .verticalScroll(rememberScrollState()),
        ) {
//            items(state.kanji) {
//            }
        }
//        FloatingActionButton(
//            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
//            onClick = {
//                navController.navigate("create_pack")
//            }
//        ) {
//            Icon(Icons.Filled.Add, contentDescription = "Add New Pack")
//        }

    }
}

@Composable
fun PackItem(modifier: Modifier = Modifier) {
    val checked = remember { mutableStateOf(false) } // temporary value
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
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
            Text("火",
                fontWeight = FontWeight.SemiBold,
                fontSize = 48.sp
            )
        }
        Row(
            modifier = Modifier
                .padding(16.dp)
                //.clip(RoundedCornerShape(16.dp))
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Elements",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider(modifier = Modifier.padding(8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column {
                        Text("Kanji Count: 12")
                        Text("Difficulty: Hard")
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