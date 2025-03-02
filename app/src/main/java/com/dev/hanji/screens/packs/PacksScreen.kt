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
import com.dev.hanji.HanjiDestination
import com.dev.hanji.AllPacks
import com.dev.hanji.MyPacks
import com.dev.hanji.components.ScreenTabRow
import com.dev.hanji.packScreens


@Composable
fun PacksScreen(modifier: Modifier = Modifier) {
   var currentScreen: HanjiDestination by remember { mutableStateOf(AllPacks) }

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
            AllPacksScreen(modifier = modifier.padding(innerPadding))
         }
         MyPacks -> {
            Log.d("Current Screen", currentScreen.title)
            MyPacksScreen(modifier = modifier.padding(innerPadding))
         }
      }

   }
}