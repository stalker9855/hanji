package com.dev.hanji.ui.screens.packs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dev.hanji.PackDetail
import com.dev.hanji.components.cardStyle
import com.dev.hanji.data.model.KanjiPackEntity
import com.dev.hanji.data.viewmodel.KanjiPackViewModel


@Composable
fun AllPacksScreen(modifier: Modifier = Modifier,
                   viewModel: KanjiPackViewModel,
                   navController: NavController) {
    val state by viewModel.state.collectAsState()
    Box {
        LazyColumn(
            modifier = modifier
                .padding(top = 8.dp)
        ) {
            items(state.kanjiPacks) { kanjiPack ->
                Box(
                    modifier = Modifier.clickable {
                        navController.navigate("${PackDetail.route}/${kanjiPack.id}")
                    },
                ) {
                    PackItem(
                        kanjiPack = kanjiPack
                    )

                }
            }
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
private fun PackItem(modifier: Modifier = Modifier, kanjiPack: KanjiPackEntity) {
    val checked = remember { mutableStateOf(false) } // temporary value
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .cardStyle()
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
