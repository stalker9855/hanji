package com.dev.hanji

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dev.hanji.screens.AboutScreen
import com.dev.hanji.screens.HomeScreen
import com.dev.hanji.screens.PacksScreen
import com.dev.hanji.screens.SettingsScreen
import com.dev.hanji.screens.user.UserScreen


@Composable
fun HanjiNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = User.route,
        modifier = modifier
    )  {
        composable(route = Home.route) {
            HomeScreen(modifier = modifier)
        }
        composable(route = Packs.route) {
            PacksScreen()
        }
        composable(route = User.route) {
            UserScreen(modifier = modifier)
        }
        composable(route = Settings.route) {
            SettingsScreen()
        }
        composable(route = About.route) {
            AboutScreen()
        }
        // navigation(
        //     startDestination = UserStats.route,
        //     route = User.route
        // ) {
        //     composable(route = UserStats.route) {
        //         UserStatsScreen()
        //     }
        //     composable(route = UserAchievements.route) {
        //         UserAchievementsScreen()
        //     }
        // }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) = this.navigate(route) {
    popUpTo(
        this@navigateSingleTopTo.graph.findStartDestination().id
    ) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = true
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)

}
