package com.dev.hanji.screens.user

import android.util.Log
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
import com.dev.hanji.components.UserTabRow
import com.dev.hanji.database.AppDatabase
import com.dev.hanji.user.UserDao
import com.dev.hanji.user.UserViewModel
import com.dev.hanji.user.UserViewModelFactory
import com.dev.hanji.userScreens


@Composable
fun UserScreen(modifier: Modifier = Modifier) {

    val userDao: UserDao = AppDatabase.getInstance(context = LocalContext.current).userDao
    val viewModel: UserViewModel = viewModel(factory = UserViewModelFactory(userDao = userDao))

    // !!! REPLACE UserAchievements TO UserStats
    var currentScreen: HanjiDestination by remember { mutableStateOf(UserAchievements) }
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
                UserInfoScreen(modifier = modifier.padding(innerPadding), viewModel = viewModel)
            }
            UserAchievements -> {
                Log.d("Current Screen", currentScreen.title)
                UserAchievementsScreen(modifier = modifier.padding(innerPadding), viewModel = viewModel)
            }
        }
    }
}