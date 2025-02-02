package com.dev.hanji

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun NavigationDrawer(navController: NavHostController, scope: CoroutineScope, drawerState: DrawerState, modifier: Modifier, content: @Composable () -> Unit) {

    val selectedItem = remember { mutableStateOf(hanjiScreens[0]) }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())
                ) {
                    Spacer(Modifier.height(12.dp))
                    Text(stringResource(R.string.app_name), modifier = Modifier.padding(16.dp))
                    HorizontalDivider(modifier = modifier.padding(16.dp))
                    hanjiScreens.forEach {
                        screen -> NavigationDrawerItem(
                            label = {
                                Text(screen.title)
                                    },
                            icon = {
                                Icon(imageVector = screen.icon, contentDescription = screen.title)
},
                            selected = selectedItem.value == screen,
                            onClick = {
                                scope.launch {
                                    selectedItem.value = screen
                                    drawerState.close()
                                    navController.navigateSingleTopTo(screen.route)
                                }
                            }
                        )
                    }
                }
            }
        },
        drawerState = drawerState,
        content = {
            content()
        }
    )
}
