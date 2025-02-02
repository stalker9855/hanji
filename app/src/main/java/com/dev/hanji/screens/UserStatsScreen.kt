package com.dev.hanji.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.hanji.R
import com.dev.hanji.UserStats


@Composable
fun UserStatsScreen(modifier: Modifier = Modifier) {
    Box {
        Column(modifier = modifier
            .fillMaxSize()
            .border(2.dp, Color.Red)
            .verticalScroll(rememberScrollState())
        )
        {
            Row(Modifier
                .fillMaxWidth()
                .border(2.dp, Color.Gray)) {
                Image(painter = painterResource(R.drawable.avatar),
                    contentDescription = "avatar", modifier = Modifier
                        .size(128.dp)
                        .clip(CircleShape))
                Column {
                    Text("Username: stalker9855")
                    Text("1 lv.")
                }
            }
            HorizontalDivider(modifier
                .height(1.dp)
                .background(Color.Gray)
                .fillMaxWidth())
            CircleStats()
            Column {
                Text("Attempts: 200")
                Text("Great: 182")
                Text("Good: 169")
                Text("Bad: 16")
                Text("Failed: 143")
            }
        }
    }
}


@Composable
fun CircleStats(modifier: Modifier = Modifier) {
    val charts = listOf(
        ChartModel(value = 182f, color = Color.Blue),
        ChartModel(value = 169f, color = Color.Red),
        ChartModel(value = 16f, color = Color.LightGray),
        ChartModel(value = 143f, color = Color.Black),
    )

    val total = charts.sumOf { it.value.toDouble() }.toFloat()

    Canvas(
        modifier = Modifier.size(200.dp).background(Color.Gray),
        onDraw = {
            var startAngle = 0f
            var sweepAngle = 0f
            charts.forEach {
                sweepAngle = (it.value / total) * 360
                drawArc(
                    color = it.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(
                        width = 4f,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
                startAngle += sweepAngle
            }
        })
}


data class ChartModel(
    val value: Float,
    val color: Color,
)
