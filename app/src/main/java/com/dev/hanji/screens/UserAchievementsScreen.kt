package com.dev.hanji.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dev.hanji.UserAchievements


@Composable
fun UserAchievementsScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(UserAchievements.title)
    }
}
@Preview
@Composable
private fun UserScreenPreview() {
    UserAchievementsScreen()
}
