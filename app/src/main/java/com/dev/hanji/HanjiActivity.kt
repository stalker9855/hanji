package com.dev.hanji

import android.os.Bundle
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dev.hanji.components.NavigationDrawer
import com.dev.hanji.components.ObserveAsEvents
import com.dev.hanji.components.SnackbarController
import com.dev.hanji.ui.theme.HanjiTheme
import com.dev.hanji.components.TopAppBarHanji
import com.dev.hanji.database.AppDatabase
import com.dev.hanji.kanji.insertKanjiFromJson
import com.dev.hanji.user.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HanjiActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HanjiTheme {
                HanjiApp()
            }
        }

//        deleteDatabase("hanji_database")
        runBlocking(Dispatchers.IO) {
            val db = AppDatabase.getInstance(this@HanjiActivity)
            val kanjiDao = db.kanjiDao
            val userDao = db.userDao
            if(kanjiDao.getKanjiCount() == 0) {
                insertKanjiFromJson(this@HanjiActivity)
            }
               userDao.insert(
                   UserEntity(
                       id = 1,
                       username = "bobross",
                       email = "bob@mail.com",
                       greatAttempts = 123,
                       goodAttempts = 321,
                       normalAttempts = 43,
                       failedAttempts = 22
                   )
               )
        }
    }
}

@Composable
private fun HanjiApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val snackbarHostState = remember {
        SnackbarHostState()
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
        NavigationDrawer(scope = scope, navController = navController, modifier = modifier, drawerState = drawerState) {
            Scaffold(modifier = Modifier,
                snackbarHost = {
                    SnackbarHost(
                        hostState = snackbarHostState
                    )
                },
                topBar = {
                    TopAppBarHanji(drawerState, scope, currentBackStackEntry,
                        onBackClick = {
                            navController.popBackStack()
                        })
                }) { innerPadding ->
                HanjiNavHost(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }

}
