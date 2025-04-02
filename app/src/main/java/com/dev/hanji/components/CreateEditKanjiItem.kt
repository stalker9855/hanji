package com.dev.hanji.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.hanji.kanji.KanjiEntity
import com.dev.hanji.kanjiPack.KanjiPackEvent

@Composable
fun CreateEditKanjiItem(modifier: Modifier = Modifier, kanji: KanjiEntity, isChecked: Boolean, onEvent: (KanjiPackEvent) -> Unit) {
    HorizontalDivider()
    ListItem(
        headlineContent = {
            Text(kanji.meanings.joinToString(", "), fontSize = 18.sp)
        },
        supportingContent = {
            Column {
                Text("On: ${kanji.readingsOn.joinToString(", ")}", fontSize = 18.sp)
                Text("Kun: ${kanji.readingsKun.joinToString(", ")}", fontSize = 18.sp)
            }
        },
        leadingContent = {
            Box(
                modifier = Modifier
                    .shadow(4.dp, shape = RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(kanji.character, textAlign = TextAlign.Center, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            }
        },
        trailingContent = {
            IconButton(
                onClick = {
                    if(!isChecked) {
                        onEvent(KanjiPackEvent.AddKanjiToPack(kanji))
                    } else {
                        onEvent(KanjiPackEvent.RemoveKanjiFromPack(kanji))
                    }
                }
            ) {
                Icon(imageVector =  if(isChecked) Icons.Filled.Delete else Icons.Filled.Add, contentDescription = "Add Kanji")
            }
        }
    )
}
