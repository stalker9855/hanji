package com.dev.hanji.ui.screens.kanji

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.hanji.components.AttemptBox
import com.dev.hanji.components.CircleStats
import com.dev.hanji.components.KanjiItem
import com.dev.hanji.components.cardStyle
import com.dev.hanji.data.state.KanjiState
import com.dev.hanji.ui.screens.draw.extractPathData
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@SuppressLint("DiscouragedApi")
@Composable
fun KanjiDetailScreen(modifier: Modifier = Modifier, state: KanjiState) {

    if (state.character == null) {
        Text("Loading . . .")
        return
    }

    val entries = state.jisho?.data?.flatMap { entry ->
        val readings = entry.japanese.mapNotNull { j ->
            j.word?.let { word ->
                val reading = j.reading
                val definition = entry.senses.firstOrNull()?.englishDefinitions?.joinToString(", ") ?: ""
                "Words: $word\nReading: $reading\nDefinition: $definition"
            }
        }
        readings
    }?.drop(2) ?: listOf("")




    val unicodeHex = state.character.unicodeHex
    val context = LocalContext.current
    val drawableId = context.resources.getIdentifier(unicodeHex, "drawable", context.packageName)
    if (drawableId == 0) {
        return
    }

    val originalPath: List<List<Offset>> =
        extractPathData(LocalContext.current, drawableId, scale = 9.4f)


    val coroutineScope = rememberCoroutineScope()
    val progressList = remember { mutableStateListOf<Animatable<Float, AnimationVector1D>>() }
    var animationJob by remember { mutableStateOf<Job?>(null) }
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Kanji", textAlign = TextAlign.Center, fontSize = 36.sp, fontWeight = FontWeight.SemiBold)

        LaunchedEffect(originalPath) {
            progressList.clear()
            progressList.addAll(originalPath.map { Animatable(0f) })

            animationJob?.cancel()
            animationJob = coroutineScope.launch {
                progressList.forEach { it.snapTo(0f) }
                progressList.forEach { progress ->
                    progress.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 850, easing = FastOutSlowInEasing)
//                        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessVeryLow)
                    )
                }
            }
        }

        Box {
            Canvas(
                modifier = modifier
                    .size(109.dp * 4)
                    .aspectRatio(1f)
                    .padding(16.dp)
                    .shadow(4.dp, shape = RoundedCornerShape(16.dp))
                    .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFFFDD0))
            ) {

                val canvasWidth = size.width
                val canvasHeight = size.height

                val centerX = canvasWidth / 2
                val centerY = canvasHeight / 2

                val dashEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

  val horizontalLines = listOf(
                    centerY to 0.4f,
                    centerY / 2 to 0.15f,
                    centerY * 1.5f to 0.15f
                )

                val verticalLines = listOf(
                    centerX to 0.35f,
                    centerX / 2 to 0.15f,
                    centerX * 1.5f to 0.15f
                )

                horizontalLines.forEach { (y, alpha) ->
                    val path = Path().apply {
                        moveTo(0f, y)
                        lineTo(canvasWidth, y)
                    }
                    drawPath(
                        path = path,
                        color = Color.Black,
                        style = Stroke(width = 5f, pathEffect = dashEffect),
                        alpha = alpha
                    )
                }

                verticalLines.forEach { (x, alpha) ->
                    val path = Path().apply {
                        moveTo(x, 0f)
                        lineTo(x, canvasHeight)
                    }
                    drawPath(
                        path = path,
                        color = Color.Black,
                        style = Stroke(width = 5f, pathEffect = dashEffect),
                        alpha = alpha
                    )
                }


                originalPath.forEachIndexed { index, pathOffsets ->
                    if(index < progressList.size) {

                        val path = Path().apply {
                            if (pathOffsets.isNotEmpty()) {
                                moveTo(pathOffsets.first().x, pathOffsets.first().y)
                                pathOffsets.drop(1).forEach { offset ->
                                    lineTo(offset.x, offset.y)
                                }
                            }
                        }
                        val pathMeasure = PathMeasure()
                        pathMeasure.setPath(path, false)
                        val pathLength = pathMeasure.length
                        val animatedPath = Path()
                        pathMeasure.getSegment(0f, pathLength * progressList[index].value, animatedPath, true)
                        drawPath(
                            path = animatedPath,
                            color = Color.Black,
                            style = Stroke(width = 25f),

                        )
                    }
                }
            }

        }

        Button(
            onClick = {
                animationJob?.cancel()
                animationJob = coroutineScope.launch {
                    progressList.forEach {it.snapTo(0f)}
                    progressList.forEach { progress ->
                        progress.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(durationMillis = 850, easing = FastOutSlowInEasing)
                        )
                    }
                }
            }
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Play kanji animation drawing"
            )
            Text("Animate")
        }

        KanjiItem(kanji = state.character)
            state.attempts?.let {
                // WTF ???
                if(it[0].attempt != null) {
                    Column(modifier = Modifier
                        .padding(12.dp)
                        .cardStyle()
                    ) {
                        Column(modifier = Modifier.padding(vertical = 8.dp).cardStyle()) {
                            AttemptBox(attempts = state.attempts)
                        }
                    }
                    CircleStats(state = state, modifier = Modifier.padding(horizontal = 12.dp))
                }


                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Words", textAlign = TextAlign.Center, fontSize = 36.sp, fontWeight = FontWeight.SemiBold)
                    entries
                        .filter { word -> word.isNotBlank() }
                        .forEach { word ->
                            Box(modifier = Modifier.padding(vertical = 4.dp)) {

                                Box(modifier = Modifier.cardStyle().padding(16.dp)) {
                                    Text(text = word)
                                }
                            }
                        }

                }

            }
    }
    }



