package com.dev.hanji.components

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dev.hanji.HanjiDestination


@Composable
fun UserTabRow(userScreens: List<HanjiDestination>,
               onTabSelected: (HanjiDestination) -> Unit,
               currentScreen: HanjiDestination
               ) {
   Surface(
       Modifier
           .wrapContentHeight()
           .fillMaxWidth()
   ) {
       Row(Modifier.selectableGroup().fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
           userScreens.forEach {screen ->
              UserTab(
                  text = screen.title,
                  icon = screen.icon,
                  onSelected = { onTabSelected(screen) },
                  selected = currentScreen == screen
              )
           }

       }
   }
}

@Composable
private fun UserTab(text: String, icon: ImageVector, onSelected: () -> Unit, selected: Boolean) {
    Row(modifier =  Modifier.padding(16.dp).selectable(
        selected = selected,
        onClick = onSelected,
        role =  Role.Tab,
    ).border(
        if (selected) 2.dp else 0.dp,
        if (selected) Color.White else Color.Transparent,
    ).clearAndSetSemantics {contentDescription = text}) {
            Icon(imageVector = icon, contentDescription = text)
            Text(text)
    }
}