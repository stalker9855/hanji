package com.dev.hanji.screens.user

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.hanji.R
import com.dev.hanji.user.UserEntity
import com.dev.hanji.user.UserViewModel


@Composable
fun UserStatsScreen(modifier: Modifier = Modifier, viewModel: UserViewModel) {
    val user by viewModel.user.collectAsState()
    val totalAttempts by viewModel.totalAttempts.collectAsState()
    Box {
        Column(modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
        )
        {
            Row(Modifier
                .fillMaxWidth()
                ) {
                Image(painter = painterResource(R.drawable.avatar),
                    contentDescription = "avatar", modifier = Modifier
                        .size(128.dp)
                        .clip(CircleShape))
                Column {
                    Text("Username: ${user?.username}")
                    Text("1 lv.")
                }
            }
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    CircleStats(user = user, totalAttempts = totalAttempts)
                    user!!.attempts.forEach {(attempts, color) ->
                        UserStat(attempts, color)
                    }
                }

        }
    }
}

/*
 MAKE AN INTERACE FOR ATTEMPT (INT, COLOR, TITLE)
 */

@Composable
private fun UserStat(attempts: Int?, color: Color, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(color)
                .clip(RoundedCornerShape(8.dp))
        )
        Text("Attempts: $attempts")
    }
}


@Composable
fun CircleStats(user: UserEntity?, totalAttempts: Int, modifier: Modifier = Modifier) {
    val textStats = stringResource(R.string.stats)
    val textMeasure = rememberTextMeasurer()
    val textStyle = TextStyle(
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )

    val textLayoutResult = textMeasure.measure(text = AnnotatedString(textStats), style = textStyle)
    val textSize = textLayoutResult.size

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(16.dp)
    ) {
        Canvas(
            modifier = Modifier.matchParentSize(),
            onDraw = {
                val padding = 16.dp.toPx()
                val size = size.minDimension - padding * 2
                var sweepAngle = 0f
                var startAngle = 0f

                user?.attempts?.forEach { (value, color) ->
                    if (value != null) {
                        sweepAngle = (value.toFloat() / totalAttempts) * 360
                    }
                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(
                            width = 16f,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        ),
                        size = Size(size, size),
                        topLeft = center - Offset(size / 2f, size / 2f)
                    )
                    startAngle += sweepAngle
                }

                drawText(
                    textMeasure, textStats,
                    topLeft = Offset(
                        (this.size.width - textSize.width) / 2f,
                        (this.size.height - textSize.height) / 2f,
                    ),
                    style = textStyle
                )
            }
        )
    }
}