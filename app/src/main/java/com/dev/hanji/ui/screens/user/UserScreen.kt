package com.dev.hanji.ui.screens.user

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dev.hanji.UserProgress
import com.dev.hanji.UserAttemptsKanji
import com.dev.hanji.UserStats
import com.dev.hanji.data.viewmodel.AchievementViewModel
import com.dev.hanji.data.factory.AchievementViewModelFactory
import com.dev.hanji.components.ScreenTabRow
import com.dev.hanji.data.database.AppDatabase
import com.dev.hanji.data.factory.KanjiAttemptFactory
import com.dev.hanji.data.factory.UserViewModelFactory
import com.dev.hanji.data.viewmodel.KanjiAttemptViewModel
import com.dev.hanji.data.viewmodel.UserViewModel
import com.dev.hanji.userScreens


@Composable
fun UserScreen(modifier: Modifier = Modifier,
               navController: NavController
               ) {

    val context = LocalContext.current
    val childNavController = rememberNavController()

    val navBackStackEntry by childNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            ScreenTabRow(
                screens = userScreens,
                onTabSelected = { screen ->
                    childNavController.navigate(screen.route) {
                        launchSingleTop = true
                        popUpTo(childNavController.graph.startDestinationId) {
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                currentScreen = currentRoute
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = childNavController,
            startDestination = UserAttemptsKanji.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(UserStats.route) {
                val userDao = AppDatabase.getInstance(context).userDao
                val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(userDao))
                UserInfoScreen(viewModel = userViewModel)
            }
            composable(UserProgress.route) {
                val achievementDao = AppDatabase.getInstance(context).achievementDao
                val achievementViewModel: AchievementViewModel = viewModel(factory = AchievementViewModelFactory(achievementDao))
                UserAchievementsScreen(viewModel = achievementViewModel, navController = navController)
            }
            composable(UserAttemptsKanji.route) {
                val attemptDao = AppDatabase.getInstance(context).kanjiAttemptDao
                val attemptViewModel: KanjiAttemptViewModel = viewModel(factory = KanjiAttemptFactory(attemptDao))
                UserKanjiAttemptScreen(viewModel = attemptViewModel)
            }
        }
    }
}
