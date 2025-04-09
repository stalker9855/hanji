package com.dev.hanji.ui.screens.user

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.hanji.R
import com.dev.hanji.data.model.UserAttempt
import com.dev.hanji.data.model.UserEntity


@Composable
fun UserInfoScreen(modifier: Modifier = Modifier, viewModel: UserViewModel) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val totalAttempts by viewModel.totalAttempts.collectAsStateWithLifecycle()
    Column {
        Column(modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
        )
        {
            Row(Modifier
                .fillMaxWidth()
                .shadow(4.dp, shape = RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(CORNER_SHAPE_SIZE))
                .background(MaterialTheme.colorScheme.surfaceContainer)
                ) {
                Image(painter = painterResource(R.drawable.avatar4),
                    contentDescription = "avatar", modifier = Modifier
                        .padding(16.dp)
                        .size(128.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                    ,
                        )
                Column(modifier = Modifier.padding(16.dp).align(Alignment.CenterVertically)) {
                    Text(text= user.username,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    HorizontalDivider(modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                    )
                    Text("Level - Beginner")
                }
            }
                Column {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .shadow(4.dp, shape = RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(CORNER_SHAPE_SIZE))
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                    ) {
                        Text(
                            text ="Attempts · 試み",
                            modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 12.dp),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        HorizontalDivider(modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                        Column(modifier = Modifier.padding(12.dp)) {
                            user.attempts.forEach {attempt ->
                                UserStat(attempt)
                            }

                        }

                    }
                    CircleStats(user = user, totalAttempts = totalAttempts)

                    Spacer(modifier = Modifier.height(20.dp))

                }

        }
    }
}


@Composable
private fun UserStat(attempt: UserAttempt, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .size(28.dp)
                .background(attempt.color)
        )
        Text("${attempt.type.value}: ${attempt.count}", modifier = modifier.padding(start = 8.dp))
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
            .shadow(4.dp, shape = RoundedCornerShape(16.dp))
            .aspectRatio(1f)
            .clip(RoundedCornerShape(CORNER_SHAPE_SIZE))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(16.dp)
    ) {
        Canvas(
            modifier = Modifier.matchParentSize(),
            onDraw = {
                val padding = 16.dp.toPx()
                val size = size.minDimension - padding * 2
                var sweepAngle: Float
                var startAngle = 0f

                user?.attempts?.forEach { attempt ->
                    sweepAngle = (attempt.count.toFloat() / totalAttempts) * 360
                    drawArc(
                        color = attempt.color,
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

private val CORNER_SHAPE_SIZE: Dp = 15.dp
