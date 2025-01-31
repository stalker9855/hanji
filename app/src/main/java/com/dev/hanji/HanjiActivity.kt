package com.dev.hanji

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.dev.hanji.ui.theme.HanjiTheme

class HanjiActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HanjiTheme {
                HanjiApp()
            }
        }
    }
}

@Composable
private fun HanjiApp() {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    NavigationDrawer(scope = scope, navController = navController) {
        Scaffold(modifier = Modifier) { innerPadding ->
            HanjiNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}