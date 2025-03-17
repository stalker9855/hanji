@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.dev.hanji

import android.util.Log
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.dev.hanji.database.AppDatabase
import com.dev.hanji.kanji.KanjiAttemptDao
import com.dev.hanji.kanji.KanjiAttemptFactory
import com.dev.hanji.kanji.KanjiAttemptViewModel
import com.dev.hanji.kanjiPack.KanjiPackDao
import com.dev.hanji.kanjiPack.KanjiPackFactory
import com.dev.hanji.kanjiPack.KanjiPackViewModel
import com.dev.hanji.screens.AboutScreen
import com.dev.hanji.screens.DrawScreen
import com.dev.hanji.screens.HomeScreen
import com.dev.hanji.screens.packs.PacksScreen
import com.dev.hanji.screens.SettingsScreen
import com.dev.hanji.screens.packs.CreateKanjiPackScreen
import com.dev.hanji.screens.packs.KanjiPackDetailScreen
import com.dev.hanji.screens.user.UserScreen

const val FAB_EXPLODE_BOUNDS_KEY = "FAB_EXPLODE_BOUNDS_KEY"

@Composable
fun HanjiNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    val kanjiPackDao: KanjiPackDao = AppDatabase.getInstance(context = LocalContext.current).kanjiPackDao
    val kanjiAttemptDao: KanjiAttemptDao = AppDatabase.getInstance(context = LocalContext.current).kanjiAttemptDao
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = Packs.route,
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
                UserScreen()
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
                Log.d("PACK ID", "$packId")
                val drawingViewModel = viewModel<DrawingViewModel>()
                val kanjiPackViewModel  = viewModel<KanjiPackViewModel>(factory = KanjiPackFactory(kanjiPackDao, packId!!))
                val kanjiAttemptViewModel  = viewModel<KanjiAttemptViewModel>(factory = KanjiAttemptFactory(kanjiAttemptDao))

                val packState by kanjiPackViewModel.packDetailState.collectAsStateWithLifecycle()

                DrawScreen(drawingViewModel = drawingViewModel, kanjiAttemptViewModel = kanjiAttemptViewModel, packState = packState)
            }
            composable(route = CreatePack.route) {
                val viewModel  = viewModel<KanjiPackViewModel>(factory = KanjiPackFactory(kanjiPackDao, 0))
                val createKanjiPackState by viewModel.createKanjiPackState.collectAsStateWithLifecycle()
                val pagedKanjiList = viewModel.pagedKanjiList.collectAsLazyPagingItems()
                CreateKanjiPackScreen(
                    pagedKanjiList = pagedKanjiList,
                    onEvent = viewModel::onEvent,
                    state = createKanjiPackState,
                    modifier = Modifier
                    .fillMaxSize()
                    .sharedBounds(
                        sharedContentState = rememberSharedContentState(
                            key = FAB_EXPLODE_BOUNDS_KEY
                        ),
                        animatedVisibilityScope = this
                    ))
            }
            composable(route = "${PackDetail.route}/{packId}",
                arguments = listOf(navArgument("packId") { type = NavType.LongType})
            ) { navBackStackEntry ->
                val packId = navBackStackEntry.arguments?.getLong("packId")
                Log.d("PACK ID DETAIL SCREEN", "$packId")
                val viewModel  = viewModel<KanjiPackViewModel>(factory = KanjiPackFactory(kanjiPackDao, packId!!))
                val packDetailState by viewModel.packDetailState.collectAsStateWithLifecycle()
                KanjiPackDetailScreen(state = packDetailState, onEvent = viewModel::onEvent, navController = navController)
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
