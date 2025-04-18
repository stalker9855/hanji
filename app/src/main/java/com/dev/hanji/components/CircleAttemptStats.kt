package com.dev.hanji.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.hanji.R
import com.dev.hanji.data.state.HasAttempts

@Composable
fun CircleStats(state: HasAttempts?, modifier: Modifier = Modifier) {
    var isTranslated by remember { mutableStateOf(false) }
    val textStats = stringResource(R.string.stats)
    val translatedTexAtStats = stringResource(R.string.stats_en)
    val displayText = if (isTranslated) translatedTexAtStats else textStats
    val textMeasure = rememberTextMeasurer()
    val textStyle = TextStyle(
        fontSize = 36.sp,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )

    val textLayoutResult = textMeasure.measure(text = AnnotatedString(displayText), style = textStyle)
    val textSize = textLayoutResult.size


    Box(
        modifier = modifier
            .cardStyle()
            .aspectRatio(1f)
            .clickToTranslate { isTranslated = !isTranslated }
    ) {
        Canvas(
            modifier = Modifier.matchParentSize(),
            onDraw = {
                val padding = 16.dp.toPx()
                val size = size.minDimension - padding * 2
                var sweepAngle: Float
                var startAngle = 0f

                state?.attempts?.forEach { attempt ->
                    sweepAngle = ((attempt.attempt?.toFloat() ?: 0f) / state.total) * 360
                    drawArc(
                        color = attempt.color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(
                            width = 24f,
                            cap = StrokeCap.Butt,
                            join = StrokeJoin.Bevel
                        ),
                        size = Size(size, size),
                        topLeft = center - Offset(size / 2f, size / 2f)
                    )
                    startAngle += sweepAngle
                }

                drawText(
                    textMeasure, displayText,
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
