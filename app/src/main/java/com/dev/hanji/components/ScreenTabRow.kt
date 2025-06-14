package com.dev.hanji.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dev.hanji.HanjiDestination


@Composable
fun ScreenTabRow(screens: List<HanjiDestination>,
                 onTabSelected: (HanjiDestination) -> Unit,
                 currentScreen: String?
               ) {
   Surface(
       modifier = Modifier.fillMaxWidth()
   ) {
       Row(modifier = Modifier.selectableGroup()
           .fillMaxWidth(),
           horizontalArrangement = Arrangement.SpaceAround) {
           screens.forEach { screen ->
              ScreenTab(
                  text = screen.title,
                  icon = screen.icon,
                  onSelected = { onTabSelected(screen) },
                  selected = currentScreen == screen.route
              )
           }

       }
   }
}

@Composable
private fun ScreenTab(text: String, icon: ImageVector, onSelected: () -> Unit, selected: Boolean) {

    Row(modifier =  Modifier
        .wrapContentHeight()
        .padding(horizontal = 8.dp, vertical = 4.dp)
        .selectable(
            selected = selected,
            onClick = onSelected,
            role =  Role.Tab,
            interactionSource = remember {MutableInteractionSource()},
            indication = null,
        )
    .clearAndSetSemantics {contentDescription = text}) {
        Row(modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
            if (selected)
                MaterialTheme.colorScheme.primaryContainer
            else
                Color.Transparent)
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(imageVector = icon, contentDescription = text)
            Spacer(modifier = Modifier.padding(4.dp))
            Text(text = text,
                color = if(selected)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.outline,
                fontWeight = if(selected) FontWeight.ExtraBold else FontWeight.Normal
            )
        }
    }
}