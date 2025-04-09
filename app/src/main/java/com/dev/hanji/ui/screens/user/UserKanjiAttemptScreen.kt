package com.dev.hanji.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.hanji.data.model.KanjiAttemptEntity
import com.dev.hanji.data.viewmodel.KanjiAttemptViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun UserKanjiAttemptScreen(modifier: Modifier = Modifier, viewModel: KanjiAttemptViewModel) {
    val state by viewModel.attemptState.collectAsStateWithLifecycle()
    var showDialogGenerate by remember { mutableStateOf(false)}
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showDialogGenerate = true
            }) {
                Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Generate Kanji Pack based on next review?")
            }
        }
    ) { _ ->
        Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
            state.attemptsList.forEach { attempt ->
                KanjiAttemptItem(attempt = attempt)
            }
        }

    }
    if(showDialogGenerate) {
        AlertDialog(
            onDismissRequest = { showDialogGenerate = false },
            title = { Text("Generate Kanji Pack") },
            text = { Text("You can create kanji pack based on next review.\nDo you want to generate?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialogGenerate = false
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialogGenerate = false }
                ) {
                    Text("No")
                }
            }
        )
    }
}

@Composable
private fun KanjiAttemptItem(modifier: Modifier = Modifier, attempt: KanjiAttemptEntity) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)
        .shadow(4.dp, shape = RoundedCornerShape(8.dp))
        .clip(RoundedCornerShape(15.dp))
        .background(MaterialTheme.colorScheme.surfaceContainer)
)
    {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = attempt.character, fontWeight = FontWeight.SemiBold, fontSize = 36.sp)
            Column {
                Text(text = "Errors on last attempt: ${attempt.errors}", fontWeight = FontWeight.Normal, fontSize = 18.sp)
                Text(text = "Last Review: ${formatTimestamp(attempt.lastReview)}", fontWeight = FontWeight.Normal, fontSize = 18.sp)
                Text(text = "Next Review: ${formatTimestamp(attempt.nextReviewDate)}", fontWeight = FontWeight.Normal, fontSize = 18.sp)
            }
        }
    }



}
private fun formatTimestamp(timestamp: Long): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
        .withZone(ZoneId.systemDefault())
    return formatter.format(Instant.ofEpochMilli(timestamp))
}
