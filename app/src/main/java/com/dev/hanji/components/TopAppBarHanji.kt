package com.dev.hanji.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import com.dev.hanji.CreatePack
import com.dev.hanji.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarHanji(drawerState: DrawerState, scope: CoroutineScope, currentBackStackEntry: State<NavBackStackEntry?>, onBackClick: () -> Unit = {}) {
    // if i would have multiple screen where arrow is needed then create listOf in hanjiDestinations
    val showBackArrow = currentBackStackEntry.value?.destination?.route == CreatePack.route
    TopAppBar(
        modifier = Modifier,
        title = { Text(text = stringResource(R.string.app_name)) },
        navigationIcon = {
            if(showBackArrow){
                IconButton(onClick =  onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Open Drawer Menu"
                    )
                }
            } else {
                IconButton(onClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Open Drawer Menu"
                    )
                }
            }
        },
    )
}
