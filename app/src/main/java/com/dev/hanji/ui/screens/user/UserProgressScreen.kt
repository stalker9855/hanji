package com.dev.hanji.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dev.hanji.KanjiDetail
import com.dev.hanji.UserProgress
import com.dev.hanji.components.KanjiProgressSlider
import com.dev.hanji.components.cardStyle
import com.dev.hanji.data.dao.KanjiWithAttemptStatus
import com.dev.hanji.data.viewmodel.ProgressViewModel


@Composable
fun UserAchievementsScreen(modifier: Modifier = Modifier, viewModel: ProgressViewModel, navController: NavController) {
    val kanjiProgress: LazyPagingItems<KanjiWithAttemptStatus> = viewModel.progress.collectAsLazyPagingItems()
    val attempts by viewModel.progressState.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        KanjiProgressSlider(kanjiProgress = kanjiProgress, attempts = attempts)
        Column(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .shadow(4.dp, shape = RoundedCornerShape(16.dp))
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
                items(kanjiProgress.itemCount) { index ->
                    val progress = kanjiProgress[index]
                    progress?.let { AchieveBox(progress = it, navController = navController) }
                }
            }

        }


        Column(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .shadow(4.dp, shape = RoundedCornerShape(16.dp))
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
                items(kanjiProgress.itemCount) { index ->
                    val progress = kanjiProgress[index]
                    progress?.let { AchieveBox(progress = it, navController = navController) }
                }
            }

        }


    }


}


@Composable
private  fun AchieveBox(
    progress: KanjiWithAttemptStatus,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Box(modifier = Modifier.cardStyle().size(SIZE_BOX).clickable {
        navController.navigate("${KanjiDetail.route}/${progress.character}")
    }
        .background(if (progress.isAttempted) MaterialTheme.colorScheme.primaryContainer else Color.Gray),
        contentAlignment = Alignment.Center) {
        Text(
            text = progress.character,
            fontWeight = FontWeight.SemiBold,
            fontSize = 32.sp,
            textAlign = TextAlign.Center)
    }
}

private val SIZE_BOX: Dp = 56.dp

