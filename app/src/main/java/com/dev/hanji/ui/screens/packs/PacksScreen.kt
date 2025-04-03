@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.dev.hanji.ui.screens.packs

import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.dev.hanji.HanjiDestination
import com.dev.hanji.AllPacks
import com.dev.hanji.CreatePack
import com.dev.hanji.FAB_EXPLODE_BOUNDS_KEY
import com.dev.hanji.MyPacks
import com.dev.hanji.components.ScreenTabRow
import com.dev.hanji.data.viewmodel.KanjiPackViewModel
import com.dev.hanji.packScreens


@Composable
fun SharedTransitionScope.PacksScreen(modifier: Modifier = Modifier,
                                      navController: NavController,
                                      animatedVisibilityScope: AnimatedVisibilityScope,
                                      viewModel: KanjiPackViewModel,
) {
   var currentScreen: HanjiDestination by remember { mutableStateOf(AllPacks) }
   Scaffold(
      topBar = {
         ScreenTabRow(
            screens = packScreens,
            onTabSelected = { screen -> currentScreen = screen },
            currentScreen = currentScreen
         )
      },
      floatingActionButton = {
         FloatingActionButton(modifier = Modifier.sharedBounds(
            rememberSharedContentState(
               key = FAB_EXPLODE_BOUNDS_KEY
            ),
            animatedVisibilityScope = animatedVisibilityScope
         ),
            onClick = {
            navController.navigate(CreatePack.route)
         }
         ) {
            Icon(
              imageVector = Icons.Filled.Add,
              contentDescription = "Add new Kanji Pack"
            )
         }
      }
   ) {
      innerPadding ->
      when(currentScreen) {
         AllPacks -> {
            Log.d("Current Screen", currentScreen.title)
            AllPacksScreen(
               modifier = modifier.padding(innerPadding),
               viewModel = viewModel,
               navController = navController
            )
         }
         MyPacks -> {
            Log.d("Current Screen", currentScreen.title)
            MyPacksScreen(modifier = modifier.padding(innerPadding))
         }
      }

   }
}