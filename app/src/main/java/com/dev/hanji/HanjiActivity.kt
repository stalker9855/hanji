package com.dev.hanji

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dev.hanji.components.NavigationDrawer
import com.dev.hanji.components.ObserveAsEvents
import com.dev.hanji.components.SnackbarController
import com.dev.hanji.ui.theme.HanjiTheme
import com.dev.hanji.components.TopAppBarHanji
import com.dev.hanji.data.DataStoreRepository
import com.dev.hanji.data.database.AppDatabase
import com.dev.hanji.data.events.DailyEvent
import com.dev.hanji.data.factory.DailyAttemptFactory
import com.dev.hanji.data.viewmodel.DailyAttemptViewModel
import com.dev.hanji.data.viewmodel.OnBoardViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HanjiActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repo = DataStoreRepository(applicationContext)
        val onBoardViewModel = OnBoardViewModel(repo)

        enableEdgeToEdge()
        setContent {
            HanjiTheme {
                HanjiApp(onBoardViewModel = onBoardViewModel)
            }
        }

//        deleteDatabase("hanji_database")



//        runBlocking(Dispatchers.IO) {
//            val db = AppDatabase.getInstance(this@HanjiActivity)
//            val kanjiDao = db.kanjiDao
//            if(kanjiDao.getKanjiCount() == 0) {
//                insertKanjiFromJson(this@HanjiActivity)
//            }
//
//        }
    }
}

@Composable
private fun HanjiApp(modifier: Modifier = Modifier, onBoardViewModel: OnBoardViewModel) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


    val dailyDao = AppDatabase.getInstance(LocalContext.current).dailyAttemptDao
    val dailyViewModel =  viewModel<DailyAttemptViewModel>(factory = DailyAttemptFactory(dailyDao))


    LaunchedEffect(Unit) {
        onBoardViewModel.readOnBoardingState.first { completed ->
            Log.d("COMPLETED? ", "$completed")
            // change to FALSE for production
            if(!completed) {
                navController.navigate(OnBoard.route) {
                    popUpTo(Splash.route) { inclusive = true }
                }
            } else {
                navController.navigate(Home.route) {
                    popUpTo(Splash.route) { inclusive = true }
                }
            }
            dailyViewModel.onEvent(DailyEvent.GetDate)
            true
        }
    }

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val snackbarHostState = remember {
        SnackbarHostState()
    }


    val currentRoute = currentBackStackEntry.value?.destination?.route
    val showBackArrow = when {
        currentRoute?.startsWith(Draw.route) == true -> true
        currentRoute == CreatePack.route -> true
        currentRoute?.startsWith(EditPack.route) == true -> true
        currentRoute?.startsWith(PackDetail.route) == true -> true
        currentRoute?.startsWith(KanjiDetail.route) == true -> true
        else -> false
    }


    ObserveAsEvents(flow = SnackbarController.events, snackbarHostState) { event ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()

            val result = snackbarHostState.showSnackbar(
                message = event.message,
                actionLabel = event.action?.name,
                duration = SnackbarDuration.Short
            )
            if(result == SnackbarResult.ActionPerformed) {
                event.action?.action?.invoke()
            }
        }

    }

    val isOnBoarding = currentRoute == OnBoard.route

    NavigationDrawer(scope = scope, navController = navController, modifier = modifier, drawerState = drawerState, gesturesEnabled = !showBackArrow) {
        Scaffold(modifier = Modifier,
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState
                )
            },
            topBar = {
                if(!isOnBoarding) {
                    TopAppBarHanji(drawerState, scope, currentBackStackEntry, showBackArrow,
                        onBackClick = {
                            navController.popBackStack()
                        })
                }
            }) { innerPadding ->
            HanjiNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
                onBoardViewModel = onBoardViewModel
            )
        }
    }

}
