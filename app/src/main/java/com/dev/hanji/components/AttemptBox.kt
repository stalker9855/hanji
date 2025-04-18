package com.dev.hanji.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.hanji.R
import com.dev.hanji.data.state.UserAttempt


@Composable
fun AttemptBox(modifier: Modifier = Modifier, attempts: List<UserAttempt>?) {
    var isTranslated by remember { mutableStateOf(false) }
    val textStats = stringResource(R.string.attempts)
    val translatedTexAtStats = stringResource(R.string.attempts_en)
    val displayText = if (isTranslated) translatedTexAtStats else textStats
    Column(
        modifier = Modifier
            .clickToTranslate { isTranslated = !isTranslated }
    ) {
        Text(
            text = displayText,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 12.dp).height(32.dp),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        HorizontalDivider(modifier =
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Column(modifier = Modifier.padding(12.dp)) {
            attempts?.forEach { attempt ->
                UserStat(attempt)
            }
        }
    }


}