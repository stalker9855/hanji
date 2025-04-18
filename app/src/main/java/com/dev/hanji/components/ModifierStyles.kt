package com.dev.hanji.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.cardStyle(): Modifier = this
    .fillMaxWidth()
    .shadow(4.dp, shape = RoundedCornerShape(16.dp))
    .clip(RoundedCornerShape(CORNER_SHAPE_SIZE))
    .background(MaterialTheme.colorScheme.surfaceContainer)


@Composable
fun Modifier.clickToTranslate(onToggle: () -> Unit): Modifier = this
    .clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onToggle()
    }



private val CORNER_SHAPE_SIZE: Dp = 15.dp
