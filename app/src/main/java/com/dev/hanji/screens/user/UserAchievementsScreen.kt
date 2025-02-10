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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.hanji.R
import com.dev.hanji.UserAchievements
import com.dev.hanji.user.UserViewModel


@Composable
fun UserAchievementsScreen(modifier: Modifier = Modifier, viewModel: UserViewModel) {
    val achievements = (1..20).toList()
        Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = UserAchievements.title + " · 実績",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "0 / 20",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 44.dp),
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(achievements) { number ->
                    AchieveBox()
                }
            }

        }

    }

@Composable
private  fun AchieveBox(modifier: Modifier = Modifier) {

    Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).size(44.dp).background(MaterialTheme.colorScheme.primaryContainer)) {
        Icon(
            painter = painterResource(id = R.drawable.achievement1),
            contentDescription = "Achievement Image",
            modifier = Modifier.padding(4.dp)
        )
    }
}

