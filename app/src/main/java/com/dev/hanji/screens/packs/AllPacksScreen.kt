package com.dev.hanji.screens.packs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun AllPacksScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(top = 8.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        PackItem()
        PackItem()
        PackItem()
        PackItem()
        PackItem()
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