package com.dev.hanji.screens.user

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dev.hanji.HanjiDestination
import com.dev.hanji.UserAchievements
import com.dev.hanji.UserStats
import com.dev.hanji.achievements.AchievementDao
import com.dev.hanji.achievements.AchievementViewModel
import com.dev.hanji.achievements.AchievementViewModelFactory
import com.dev.hanji.components.ScreenTabRow
import com.dev.hanji.database.AppDatabase
import com.dev.hanji.user.UserDao
import com.dev.hanji.user.UserViewModel
import com.dev.hanji.user.UserViewModelFactory
import com.dev.hanji.userScreens


@Composable
fun UserScreen(modifier: Modifier = Modifier) {

    val userDao: UserDao = AppDatabase.getInstance(context = LocalContext.current).userDao
    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(userDao = userDao))

    val achievementDao: AchievementDao = AppDatabase.getInstance(context = LocalContext.current).achievementDao
    val achievementViewModel: AchievementViewModel = viewModel(factory = AchievementViewModelFactory(achievementDao))

    var currentScreen: HanjiDestination by remember { mutableStateOf(UserStats) }
    Scaffold(
        topBar = {
            ScreenTabRow(
                screens = userScreens,
                onTabSelected = { screen -> currentScreen = screen },
                currentScreen = currentScreen
            )
        }

    ) { innerPadding ->
        when(currentScreen) {
            UserStats ->  {
                UserInfoScreen(modifier = modifier.padding(innerPadding), viewModel = userViewModel)
            }
            UserAchievements -> {
                UserAchievementsScreen(modifier = modifier.padding(innerPadding), viewModel = achievementViewModel)
            }
        }
    }
}