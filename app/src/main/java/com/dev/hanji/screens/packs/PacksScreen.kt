package com.dev.hanji.screens.packs

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
import androidx.navigation.NavController
import com.dev.hanji.HanjiDestination
import com.dev.hanji.AllPacks
import com.dev.hanji.MyPacks
import com.dev.hanji.components.ScreenTabRow
import com.dev.hanji.database.AppDatabase
import com.dev.hanji.kanjiPack.KanjiPackDao
import com.dev.hanji.kanjiPack.KanjiPackFactory
import com.dev.hanji.kanjiPack.KanjiPackViewModel
import com.dev.hanji.packScreens


@Composable
fun PacksScreen(modifier: Modifier = Modifier,
                navController: NavController) {
   var currentScreen: HanjiDestination by remember { mutableStateOf(AllPacks) }

   val kanjiPackDao: KanjiPackDao = AppDatabase.getInstance(context = LocalContext.current).kanjiPackDao
   val viewModel: KanjiPackViewModel = viewModel(factory = KanjiPackFactory(kanjiPackDao))

   Scaffold(
      topBar = {
         ScreenTabRow(
            screens = packScreens,
            onTabSelected = { screen -> currentScreen = screen },
            currentScreen = currentScreen
         )
      }
   ) {
      innerPadding ->
      when(currentScreen) {
         AllPacks -> {
            Log.d("Current Screen", currentScreen.title)
            AllPacksScreen(
               modifier = modifier.padding(innerPadding),
               viewModel = viewModel,
               navController = navController)
         }
         MyPacks -> {
            Log.d("Current Screen", currentScreen.title)
            MyPacksScreen(modifier = modifier.padding(innerPadding))
         }
      }

   }
}