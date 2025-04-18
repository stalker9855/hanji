package com.dev.hanji.components

import android.util.Log
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
import com.dev.hanji.Draw
import com.dev.hanji.EditPack
import com.dev.hanji.KanjiDetail
import com.dev.hanji.PackDetail
import com.dev.hanji.R
import com.dev.hanji.arrowBackScreens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarHanji(drawerState: DrawerState, scope: CoroutineScope, currentBackStackEntry: State<NavBackStackEntry?>, showBackArrow: Boolean, onBackClick: () -> Unit = {}) {
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

                },
                    enabled = !drawerState.isOpen) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Open Drawer Menu"
                    )
                }
            }
        },
    )
}
