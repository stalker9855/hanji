package com.dev.hanji

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.dev.hanji.components.hanjiScreens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun NavigationDrawer(navController: NavHostController, scope: CoroutineScope, content: @Composable () -> Unit) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val selectedItem = remember { mutableStateOf(hanjiScreens[0]) }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())
                ) {
                    Spacer(Modifier.height(12.dp))
                    Text("Hanji", modifier = Modifier.padding(16.dp))
                    HorizontalDivider()
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
