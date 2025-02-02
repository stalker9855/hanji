package com.dev.hanji.screens

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.hanji.HanjiDestination
import com.dev.hanji.UserAchievements
import com.dev.hanji.UserStats
import com.dev.hanji.components.UserTabRow
import com.dev.hanji.userScreens


@Composable
fun UserScreen(modifier: Modifier = Modifier) {
    var currentScreen: HanjiDestination by remember { mutableStateOf(UserStats) }
    Scaffold(
        topBar = {
            UserTabRow(
                userScreens = userScreens,
                onTabSelected = { screen -> currentScreen = screen },
                currentScreen = currentScreen
            )
        }

    ) { innerPadding ->
        when(currentScreen) {
            UserStats ->  {
                Log.d("Current Screen", currentScreen.title)
                UserStatsScreen(modifier = modifier)
            }
            UserAchievements -> {
                Log.d("Current Screen", currentScreen.title)
                UserAchievementsScreen(modifier = modifier)
            }
        }
    }
}

@Preview
@Composable
private fun UserScreenPreview() {
    UserScreen()
}