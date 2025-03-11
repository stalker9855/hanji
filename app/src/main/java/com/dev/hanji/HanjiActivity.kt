package com.dev.hanji

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dev.hanji.components.NavigationDrawer
import com.dev.hanji.ui.theme.HanjiTheme
import com.dev.hanji.components.TopAppBarHanji
import com.dev.hanji.database.AppDatabase
import com.dev.hanji.kanji.insertKanjiFromJson
import kotlinx.coroutines.Dispatchers
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

        runBlocking(Dispatchers.IO) {
            val db = AppDatabase.getInstance(this@HanjiActivity)
            val kanjiDao = db.kanjiDao
            if(kanjiDao.getKanjiCount() == 0) {
                insertKanjiFromJson(this@HanjiActivity)
            }
        }
    }
}

@Composable
private fun HanjiApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
        NavigationDrawer(scope = scope, navController = navController, modifier = modifier, drawerState = drawerState) {
            Scaffold(modifier = Modifier,
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
