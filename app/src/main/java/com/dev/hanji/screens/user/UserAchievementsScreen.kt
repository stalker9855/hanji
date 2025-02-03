package com.dev.hanji.screens.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dev.hanji.UserAchievements
import com.dev.hanji.user.UserViewModel


@Composable
fun UserAchievementsScreen(modifier: Modifier = Modifier, viewModel: UserViewModel) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(UserAchievements.title)
    }
}
