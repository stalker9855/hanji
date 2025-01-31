package com.dev.hanji

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dev.hanji.components.About
import com.dev.hanji.components.Home
import com.dev.hanji.components.Packs
import com.dev.hanji.components.Settings
import com.dev.hanji.components.User
import com.dev.hanji.screens.AboutScreen
import com.dev.hanji.screens.HomeScreen
import com.dev.hanji.screens.PacksScreen
import com.dev.hanji.screens.SettingsScreen
import com.dev.hanji.screens.UserScreen


@Composable
fun HanjiNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier
    )  {
        composable(route = Home.route) {
            HomeScreen()
        }
        composable(route = Packs.route) {
            PacksScreen()
        }
        composable(route = User.route) {
            UserScreen()
        }
        composable(route = Settings.route) {
            SettingsScreen()
        }
        composable(route = About.route) {
            AboutScreen()
        }
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