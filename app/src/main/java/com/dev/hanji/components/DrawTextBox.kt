package com.dev.hanji.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun DrawTextBox(modifier: Modifier = Modifier, text: String, textSize: Int = 18, color: Color = MaterialTheme.colorScheme.surfaceContainer) {
    Box(modifier = Modifier
        .clip(RoundedCornerShape(8.dp))
        .background(color = color)
        .padding(4.dp)
    ) {
        Text(
            text = text,
            fontSize = textSize.sp
        )
    }

}
