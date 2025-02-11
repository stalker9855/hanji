package com.dev.hanji.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.hanji.UserAchievements
import com.dev.hanji.achievements.AchievementEntity
import com.dev.hanji.achievements.AchievementViewModel


@Composable
fun UserAchievementsScreen(modifier: Modifier = Modifier, viewModel: AchievementViewModel) {
    val achievements by viewModel.achievements.collectAsStateWithLifecycle()
    val completedAchievements by viewModel.completedAchievements.collectAsStateWithLifecycle()
    var sliderPosition by remember { mutableFloatStateOf(completedAchievements.toFloat()) }

        Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(16.dp)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = UserAchievements.title + " · 実績",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "Progress: $completedAchievements / ${achievements.size}",
                    fontSize = 20.sp,
                    // fontWeight = FontWeight.Bold
                )
                LaunchedEffect(completedAchievements) {
                    sliderPosition = completedAchievements.toFloat()
                }
                Slider(
                    value = sliderPosition,
                    onValueChange = {  },
                    enabled = false,
                    colors = SliderDefaults.colors(
                        disabledThumbColor = Color.Transparent,
                        disabledActiveTrackColor = MaterialTheme.colorScheme.secondary,
                        disabledInactiveTickColor = Color.Transparent
                    ),
                    steps = achievements.size - 1,
                    valueRange = 0f..achievements.size.toFloat()
                )

            }


            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(16.dp)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = SIZE_BOX),
                    modifier = modifier.fillMaxSize(),
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(achievements) { achievement ->
                        AchieveBox(achievement = achievement)
                    }
                }

            }
        }

    }

@Composable
private  fun AchieveBox(achievement: AchievementEntity, modifier: Modifier = Modifier) {
    Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).size(SIZE_BOX)
        .background(if (achievement.isCompleted) MaterialTheme.colorScheme.primaryContainer else Color.Gray)) {
        Icon(
            painter = painterResource(id = achievement.imageResourceId!!),
            contentDescription = achievement.condition,
            modifier = Modifier.padding(4.dp)
        )
    }
}

private val SIZE_BOX: Dp = 56.dp

