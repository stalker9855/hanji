@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.dev.hanji

import android.util.Log
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.dev.hanji.data.dao.AchievementDao
import com.dev.hanji.data.database.AppDatabase
import com.dev.hanji.data.dao.KanjiAttemptDao
import com.dev.hanji.data.dao.KanjiDao
import com.dev.hanji.data.factory.KanjiAttemptFactory
import com.dev.hanji.data.viewmodel.KanjiAttemptViewModel
import com.dev.hanji.data.dao.KanjiPackDao
import com.dev.hanji.data.dao.UserDao
import com.dev.hanji.data.factory.AchievementViewModelFactory
import com.dev.hanji.data.factory.KanjiFactory
import com.dev.hanji.data.factory.KanjiPackFactory
import com.dev.hanji.data.viewmodel.AchievementViewModel
import com.dev.hanji.data.viewmodel.KanjiPackViewModel
import com.dev.hanji.ui.screens.about.AboutScreen
import com.dev.hanji.ui.screens.draw.DrawScreen
import com.dev.hanji.ui.screens.home.HomeScreen
import com.dev.hanji.ui.screens.packs.PacksScreen
import com.dev.hanji.ui.screens.settings.SettingsScreen
import com.dev.hanji.data.viewmodel.DrawingViewModel
import com.dev.hanji.data.viewmodel.KanjiViewModel
import com.dev.hanji.ui.screens.kanji.KanjiAllScreen
import com.dev.hanji.ui.screens.packs.CreateKanjiPackScreen
import com.dev.hanji.ui.screens.packs.EditKanjiPackScreen
import com.dev.hanji.ui.screens.packs.KanjiPackDetailScreen
import com.dev.hanji.ui.screens.user.UserAchievementsScreen
import com.dev.hanji.ui.screens.user.UserInfoScreen
import com.dev.hanji.ui.screens.user.UserKanjiAttemptScreen
import com.dev.hanji.ui.screens.user.UserScreen
import com.dev.hanji.ui.screens.user.UserViewModel
import com.dev.hanji.ui.screens.user.UserViewModelFactory

const val FAB_EXPLODE_BOUNDS_KEY = "FAB_EXPLODE_BOUNDS_KEY"

@Composable
fun HanjiNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    val kanjiPackDao: KanjiPackDao = AppDatabase.getInstance(context = LocalContext.current).kanjiPackDao
    val kanjiAttemptDao: KanjiAttemptDao = AppDatabase.getInstance(context = LocalContext.current).kanjiAttemptDao
    val kanjiDao: KanjiDao = AppDatabase.getInstance(context = LocalContext.current).kanjiDao
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = User.route,
            modifier = modifier
        )  {
            composable(route = Home.route) {
                HomeScreen()
            }
            composable(route = Packs.route) {
                val viewModel  = viewModel<KanjiPackViewModel>(factory = KanjiPackFactory(kanjiPackDao, 0))
                PacksScreen(navController = navController, viewModel = viewModel, animatedVisibilityScope = this
                    )
            }
            composable(route = User.route) {
                UserScreen(
                )
            }
            composable(route = Settings.route) {
                SettingsScreen()
            }
            composable(route = About.route) {
                AboutScreen()
            }
            composable(route = "${Draw.route}/{packId}",
                arguments = listOf(navArgument(name = "packId") {type = NavType.LongType} )
            ) { navBackStackEntry ->
                val packId = navBackStackEntry.arguments?.getLong("packId")
                val drawingViewModel = viewModel<DrawingViewModel>()
                val kanjiPackViewModel  = viewModel<KanjiPackViewModel>(factory = KanjiPackFactory(kanjiPackDao, packId!!))
                val kanjiAttemptViewModel  = viewModel<KanjiAttemptViewModel>(factory = KanjiAttemptFactory(kanjiAttemptDao))

                val packState by kanjiPackViewModel.packDetailState.collectAsStateWithLifecycle()

               DrawScreen(drawingViewModel = drawingViewModel, kanjiAttemptViewModel = kanjiAttemptViewModel, packState = packState, onEvent = kanjiAttemptViewModel::onEvent, navController =  navController)
            }
            composable(route = CreatePack.route) {
                val viewModel  = viewModel<KanjiPackViewModel>(factory = KanjiPackFactory(kanjiPackDao, 0))
                val createKanjiPackState by viewModel.createKanjiPackState.collectAsStateWithLifecycle()
                val pagedKanjiList = viewModel.pagedKanjiList.collectAsLazyPagingItems()
                CreateKanjiPackScreen(
                    pagedKanjiList = pagedKanjiList,
                    onEvent = viewModel::onEvent,
                    state = createKanjiPackState,
                    navController = navController,
                    modifier = Modifier
                    .fillMaxSize()
                    .sharedBounds(
                        sharedContentState = rememberSharedContentState(
                            key = FAB_EXPLODE_BOUNDS_KEY
                        ),
                        animatedVisibilityScope = this
                    ))
            }
            composable(route = "${EditPack.route}/{state}",
                arguments = listOf(navArgument("state") {type = NavType.LongType})
            ) { navBackStackEntry ->
                val packId =  navBackStackEntry.arguments?.getLong("packId") ?: return@composable
                val viewModel = viewModel<KanjiPackViewModel>(factory = KanjiPackFactory(kanjiPackDao, packId))
//                val viewModel = remember(packId) {
//                    ViewModelProvider(
//                        owner = navBackStackEntry,
//                        factory = KanjiPackFactory(kanjiPackDao, packId)
//                    )[KanjiPackViewModel::class.java]
//                }
                val state by viewModel.editKanjiPackState.collectAsStateWithLifecycle()
                val pagedKanjiList = viewModel.pagedKanjiList.collectAsLazyPagingItems()
                EditKanjiPackScreen(state = state, onEvent = viewModel::onEvent, pagedKanjiList = pagedKanjiList, navController = navController)
            }
            composable(route = "${PackDetail.route}/{packId}",
                arguments = listOf(navArgument("packId") { type = NavType.LongType})
            ) { navBackStackEntry ->
                val packId = navBackStackEntry.arguments?.getLong("packId")
                val viewModel  = viewModel<KanjiPackViewModel>(factory = KanjiPackFactory(kanjiPackDao, packId!!))
                val packDetailState by viewModel.packDetailState.collectAsStateWithLifecycle()
                KanjiPackDetailScreen(state = packDetailState, onEvent = viewModel::onEvent, navController = navController)
            }
            composable(route = KanjiAll.route) {
                val viewModel = viewModel<KanjiViewModel>(factory = KanjiFactory(kanjiDao))
                val kanjiList = viewModel.kanjiList.collectAsLazyPagingItems()
                KanjiAllScreen(kanjiList = kanjiList, onEvent = viewModel::onEvent)
            }
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

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)

}
