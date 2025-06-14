package com.dev.hanji.ui.screens.user

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dev.hanji.UserProgress
import com.dev.hanji.UserAttemptsKanji
import com.dev.hanji.UserStats
import com.dev.hanji.data.viewmodel.ProgressViewModel
import com.dev.hanji.data.factory.ProgressFactory
import com.dev.hanji.components.ScreenTabRow
import com.dev.hanji.data.DataStoreRepository
import com.dev.hanji.data.database.AppDatabase
import com.dev.hanji.data.factory.KanjiAttemptFactory
import com.dev.hanji.data.factory.UserFactory
import com.dev.hanji.data.viewmodel.KanjiAttemptViewModel
import com.dev.hanji.data.viewmodel.UserViewModel
import com.dev.hanji.userScreens


@Composable
fun UserScreen(modifier: Modifier = Modifier,
               navController: NavController,
               repository: DataStoreRepository
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
            startDestination = UserStats.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(UserStats.route) {
                val userDao = AppDatabase.getInstance(context).userDao
                val userViewModel: UserViewModel = viewModel(factory = UserFactory(userDao, repository = repository))
                val state by userViewModel.state.collectAsStateWithLifecycle()
                UserInfoScreen(state = state, onEvent = userViewModel::onEvent)
            }
            composable(UserProgress.route) {
                val progressDao = AppDatabase.getInstance(context).progressDao
                val progressViewModel: ProgressViewModel = viewModel(factory = ProgressFactory(progressDao))
                UserAchievementsScreen(viewModel = progressViewModel, navController = navController)
            }
            composable(UserAttemptsKanji.route) {
                val attemptDao = AppDatabase.getInstance(context).kanjiAttemptDao
                val attemptViewModel: KanjiAttemptViewModel = viewModel(factory = KanjiAttemptFactory(attemptDao))
                UserKanjiAttemptScreen(viewModel = attemptViewModel, navController = navController)
            }
        }
    }
}
