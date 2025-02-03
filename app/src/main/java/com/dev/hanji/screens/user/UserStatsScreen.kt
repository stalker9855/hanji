package com.dev.hanji.screens.user

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
                    Text("Username: ${user?.username}")
                    Text("1 lv.")
                }
            }
            HorizontalDivider(modifier
                .height(1.dp)
                .background(Color.Gray)
                .fillMaxWidth())
            CircleStats(user = user, totalAttempts = totalAttempts)
            Column {
                Text("Attempts: $totalAttempts")
                Text("Great: ${user?.greatAttempts}")
                Text("Good: ${user?.goodAttempts}")
                Text("Bad: ${user?.badAttempts}")
                Text("Failed: ${user?.errorAttempts}")
            }
        }
    }
}


@Composable
fun CircleStats(user: UserEntity?, totalAttempts: Int, modifier: Modifier = Modifier) {

    val attempts = listOf(
        Pair(user?.greatAttempts, Color.Green),
        Pair(user?.goodAttempts, Color.Yellow),
        Pair(user?.badAttempts, Color.Red),
        Pair(user?.errorAttempts, Color.Black),
    )

    Canvas(
        modifier = Modifier.size(200.dp).background(Color.Gray),
        onDraw = {
            var sweepAngle = 0f
            var startAngle = 0f

            attempts.forEach { (value, color) ->
                if (value != null) {
                    sweepAngle = (value.toFloat() / totalAttempts) * 360
                }
                drawArc(
                    color = color,
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
