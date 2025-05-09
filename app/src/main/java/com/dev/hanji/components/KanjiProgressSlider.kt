package com.dev.hanji.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import com.dev.hanji.UserProgress
import com.dev.hanji.data.dao.KanjiWithAttemptStatus
import com.dev.hanji.data.state.KanjiProgressState


@Composable
fun KanjiProgressSlider(modifier: Modifier = Modifier,
                        kanjiProgress: LazyPagingItems<KanjiWithAttemptStatus>,
                        attempts: KanjiProgressState
                        ) {

    val sliderPosition by remember { mutableFloatStateOf(kanjiProgress.itemCount.toFloat()) }
        Column(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .shadow(4.dp, shape = RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = UserProgress.title + " · 実績",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "${attempts.kanjiProgressState?.attempted} / ${attempts.kanjiProgressState?.total}",
                fontSize = 20.sp,
            )
            Slider(
                value = sliderPosition,
                onValueChange = {  },
                enabled = false,
                colors = SliderDefaults.colors(
                    disabledThumbColor = Color.Transparent,
                    disabledActiveTrackColor = MaterialTheme.colorScheme.secondary,
                    disabledInactiveTickColor = Color.Transparent
                ),
                steps = kanjiProgress.itemCount,
                valueRange = 0f..kanjiProgress.itemCount.toFloat()
            )

        }



    }
