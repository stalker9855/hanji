package com.dev.hanji.screens

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.PathParser
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.dev.hanji.DrawingAction
import com.dev.hanji.DrawingViewModel
import com.dev.hanji.kanji.KanjiAttemptViewModel
import com.dev.hanji.kanji.KanjiEntity
import com.dev.hanji.kanjiPack.KanjiPackStateById
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParser
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.min


@SuppressLint("DiscouragedApi")
@Composable
fun DrawScreen(modifier: Modifier = Modifier,
               drawingViewModel: DrawingViewModel,
               kanjiAttemptViewModel: KanjiAttemptViewModel,
               packState: KanjiPackStateById?,
               navController: NavController) {
    if (packState?.kanjiPackWithKanjiList == null) {
        Text("Loading . . .")
        return
    }

    val attemptState by kanjiAttemptViewModel.state.collectAsStateWithLifecycle()
    val state by drawingViewModel.state.collectAsStateWithLifecycle()

    val currentIndex = remember { mutableIntStateOf(0) }
    val currentKanji = packState.kanjiPackWithKanjiList.kanjiList.getOrNull(currentIndex.intValue)

    val unicodeHex = currentKanji?.unicodeHex
    val context = LocalContext.current
    val drawableId = context.resources.getIdentifier(unicodeHex, "drawable", context.packageName)
    if (drawableId == 0) {
        return
    }

    val originalPath: List<List<Offset>> =
        extractPathData(LocalContext.current, drawableId)
    var currentOriginalPath by remember { mutableStateOf(originalPath) }

    var isKanjiCompleted by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val progressList = remember { mutableStateListOf<Animatable<Float, AnimationVector1D>>() }
    var animationJob by remember { mutableStateOf<Job?>(null) }

    LaunchedEffect(currentOriginalPath) {
        progressList.clear()
        progressList.addAll(currentOriginalPath.map { Animatable(0f) })
    }


    var initialIndex by remember { mutableIntStateOf(0) }
    var indexOriginalPath by remember { mutableIntStateOf(initialIndex) }
    val matchedPath = remember { mutableStateListOf<List<Offset>>() }

    var showEndDialog by remember { mutableStateOf(false) }


    var showDiscardDialog by remember { mutableStateOf(false) }
    BackHandler { showDiscardDialog = true }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text("Draw kanji")
                Text("Meaning: ${currentKanji!!.meanings.joinToString(", ")}")
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    enabled = isKanjiCompleted,
                    onClick = {
                        if (currentIndex.intValue == packState.kanjiPackWithKanjiList.kanjiList.lastIndex) {
                            showEndDialog = true
                        } else {
                            animationJob?.cancel()
                            isKanjiCompleted = false
                            currentIndex.intValue =
                                (currentIndex.intValue + 1) % packState.kanjiPackWithKanjiList.kanjiList.size
                            val newKanji =
                                packState.kanjiPackWithKanjiList.kanjiList.getOrNull(currentIndex.intValue)
                            val newUnicodeHex = newKanji?.unicodeHex
                            val newDrawableId = context.resources.getIdentifier(
                                newUnicodeHex,
                                "drawable",
                                context.packageName
                            )
                            if (newDrawableId != 0) {
                                currentOriginalPath = extractPathData(context, newDrawableId)
                                initialIndex = 0
                                indexOriginalPath = 0
                                Log.d("current original path", "$currentOriginalPath")
                                matchedPath.clear()
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next Kanji"
                    )
                }
                Button(
                    onClick = {
                        animationJob?.cancel()
                        animationJob = coroutineScope.launch {
                            progressList.forEach {it.snapTo(0f)}
                            progressList.forEach { progress ->
                                progress.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(durationMillis = 850, easing = LinearEasing)
                            )
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Play kanji animation drawing"
                    )
                }
                ModalBottomSheetSample(kanji = currentKanji)
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        }

        Box {
            Canvas(
                modifier = modifier
                    .size(109.dp * 3)
                    .aspectRatio(1f)
                    .padding(16.dp)
                    .background(Color.White)
                    .border(width = 2.dp, color = Color.Black)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = {
                                drawingViewModel.onAction(DrawingAction.OnNewPathStart)
                            },
                            onDragEnd = {
                                val epsilon = 5f
                                val simplifiedPath: List<Offset> =
                                    if (state.currentPath?.path!!.isNotEmpty()) {
                                        ramerDouglasPeucker(state.currentPath?.path!!, epsilon)
                                    } else {
                                        state.currentPath?.path!!
                                    }
                                drawingViewModel.onAction(DrawingAction.OnPathEnd)
                                Log.d("Original Size", "${currentOriginalPath}")
                                if (indexOriginalPath != currentOriginalPath.size) {
                                    val isCorrect =
                                        compareWithDTW(
                                            originalPath = currentOriginalPath[indexOriginalPath],
                                            currentPath = simplifiedPath
                                        )

                                    if (isCorrect && currentOriginalPath[indexOriginalPath] !in matchedPath) {
                                        matchedPath.add(currentOriginalPath[indexOriginalPath])
                                        indexOriginalPath++
                                    }
                                    if (indexOriginalPath == currentOriginalPath.size) {
                                        indexOriginalPath = 0
                                        isKanjiCompleted = true
                                    }
                                }
                            },
                            onDrag = { change, _ ->
                                drawingViewModel.onAction(DrawingAction.OnDraw(change.position))
                            },
                            onDragCancel = {
                                drawingViewModel.onAction(DrawingAction.OnPathEnd)
                            }
                        )
                    }
            ) {

                val canvasWidth = size.width
                val canvasHeight = size.height

                val centerX = canvasWidth / 2
                val centerY = canvasHeight / 2

                val dashEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

                val horizontalLine = Path().apply {
                    moveTo(0f, centerY)
                    lineTo(canvasWidth, centerY)
                }

                val verticalLine = Path().apply {
                    moveTo(centerX, 0f)
                    lineTo(centerX, canvasHeight)
                }

                drawPath(
                    path = horizontalLine,
                    color = Color.Black,
                    style = Stroke(width = 5f, pathEffect = dashEffect),
                    alpha = 0.15f
                )

                drawPath(
                    path = verticalLine,
                    color = Color.Black,
                    style = Stroke(width = 5f, pathEffect = dashEffect),
                    alpha = 0.15f
                )


                state.currentPath?.let {
                    drawPath(
                        path = it.path,
                        color = it.color,

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
                            alpha = 0.2f

                        )
                    }
                }

                matchedPath.forEach { pathOffsets ->
                    val path = Path().apply {
                        if (pathOffsets.isNotEmpty()) {
                            moveTo(pathOffsets.first().x, pathOffsets.first().y)
                            pathOffsets.drop(1).forEach { offset ->
                                lineTo(offset.x, offset.y)
                            }
                        }
                    }
                    drawPath(
                        path = path,
                        color = Color.Black,
                        style = Stroke(width = 25f)
                    )
                }


            }

        }

    }

    if(showEndDialog) {
        AlertDialog(
            onDismissRequest = {showEndDialog = false},
            title = { Text("Pack completed!")},
            text = { Text("You are successfully completed pack. Omedetou")},
            confirmButton = {
                Button(onClick = {
                    showEndDialog = false
                    navController.popBackStack()
                }) {
                    Text("Ok")
                }
            }


        )
    }
    if (showDiscardDialog) {
        AlertDialog(
            onDismissRequest = { showDiscardDialog = false },
            title = { Text("End Practice?") },
            text = { Text("Are you sure to end practice? This will discard stats.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDiscardDialog = false
                        navController.popBackStack()
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDiscardDialog = false }
                ) {
                    Text("No")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetSample(kanji: KanjiEntity?) {
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val skipPartiallyExpanded by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val bottomSheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)

    // App content
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Button(
            onClick = { openBottomSheet = !openBottomSheet },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "Show info"
            )
        }
    }
    if (openBottomSheet) {

        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = bottomSheetState,
        ) {
                Column(
                    Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        onClick = {
                            scope
                                .launch { bottomSheetState.hide() }
                                .invokeOnCompletion {
                                    if (!bottomSheetState.isVisible) {
                                        openBottomSheet = false
                                    }
                                }
                        }
                    ) {
                        Text("Hide")
                    }
                    KanjiItem(kanji = kanji)
            }
        }
    }
}

@Composable
private fun KanjiItem(modifier: Modifier = Modifier, kanji: KanjiEntity?) {
    HorizontalDivider()
    ListItem(
        headlineContent = {
            kanji?.meanings?.joinToString(", ")?.let { Text(it, fontSize = 24.sp) }
        },
        supportingContent = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("On: ${kanji?.readingsOn?.joinToString(", ")}", fontSize = 24.sp, lineHeight = 32.sp)
                Text("Kun: ${kanji?.readingsKun?.joinToString(", ")}", fontSize = 24.sp, lineHeight = 32.sp)
            }
        },
        leadingContent = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                kanji?.character?.let { Text(it, textAlign = TextAlign.Center, fontSize = 32.sp, fontWeight = FontWeight.SemiBold) }
            }
        })
}


private fun DrawScope.drawPath(
    path: List<Offset>,
    color: Color,
    thickness: Float = 10f
) {
    val smoothedPath = Path().apply {
        if(path.isNotEmpty()) {
            moveTo(path.first().x, path.first().y)

            val smoothness = 3
            for (i in 1..path.lastIndex) {
                val from = path[i - 1]
                val to = path[i]
                val dx = abs(from.x - to.x)
                val dy = abs(from.y - to.y)
                if(dx >= smoothness || dy >= smoothness) {
                    quadraticTo(
                        x1 = (from.x + to.x) / 2f,
                        y1 = (from.y + to.y) / 2f,
                        x2 = to.x,
                        y2 = to.y

                    )
                }
            }
        }
    }

    drawPath(
        path = smoothedPath,
        color = color,
        style = Stroke(
            width = thickness,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}

private fun extractPathData(context: Context, resId: Int, scale: Float = 7.2f): List<List<Offset>> {
    val parser = context.resources.getXml(resId)
    val pathDataList = mutableListOf<String>()

    try {
        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            if (parser.eventType == XmlPullParser.START_TAG && parser.name == "path") {
                for (i in 0 until parser.attributeCount) {
                    if (parser.getAttributeName(i) == "pathData") {
                        val pathData: String = parser.getAttributeValue(i)
                        pathDataList.add(pathData)
                        break
                    }
                }
            }
            parser.next()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return parsePathData(pathDataList = pathDataList, scale = scale)
}

private fun parsePathData(pathDataList: MutableList<String>, numPoints: Int = 20, scale: Float): List<List<Offset>> {
    val paths = mutableListOf<List<Offset>>()

    for (pathData in pathDataList) {
        val points = mutableListOf<Offset>()
        val path = PathParser.createPathFromPathData(pathData)
        val pathMeasure = android.graphics.PathMeasure(path, false)
        val pos = FloatArray(2)

        val pathLength = pathMeasure.length

        for (i in 0..numPoints) {
            val distance = i * pathLength / numPoints
            if (pathMeasure.getPosTan(distance, pos, null)) {
                points.add(Offset(pos[0] * scale, pos[1] * scale))
            }
        }
        paths.add(points)
    }

    return paths
}


private fun compareWithDTW(originalPath: List<Offset>, currentPath: List<Offset>): Boolean {

    val matrix = Array(originalPath.count() + 1) {
            i ->
        Array(currentPath.count() + 1) {
                j ->
            when {
                i == 0 && j == 0 -> 0f
                i == 0 || j == 0 -> Float.POSITIVE_INFINITY
                else -> 0f
            }
        }
    }

    originalPath.forEachIndexed { i, point ->
        currentPath.forEachIndexed {
                j, otherPoint ->
            val cost = cost(p1 = point, p2 = otherPoint)

            val bestPreviousCost = min(
                matrix[i + 1][j],
                matrix[i][j + 1],
                matrix[i][j]
            )

            matrix[i + 1][j + 1] = bestPreviousCost + cost
        }

    }

    val value = (matrix[originalPath.count()][currentPath.count()] / 100)
    Log.d("value", "$value")
    return  value < 16.33
}

private fun cost(p1: Offset, p2: Offset): Float {
    return hypot(p1.x - p2.x, p1.y - p2.y)
}

private fun min(a: Float, b: Float, c: Float): Float = min(a, min(b,c))




private fun perpendicularDistance(point: Offset, start: Offset, end: Offset): Float {
    val num = (end.y - start.y) * point.x - (end.x - start.x) * point.y + end.x * start.y - end.y * start.x
    val denom = hypot(end.x - start.x, end.y - start.y)
    return abs(num) / denom
}

private fun ramerDouglasPeucker(points: List<Offset>, epsilon: Float): List<Offset> {
    if (points.size < 2) return points

    var maxDist = 0f
    var index = 0
    for (i in 1 until points.size - 1) {
        val dist = perpendicularDistance(points[i], points[0], points[points.size - 1])
        if (dist > maxDist) {
            maxDist = dist
            index = i
        }
    }

    return if (maxDist < epsilon) {
        listOf(points[0], points[points.size - 1])
    } else {
        val firstHalf = ramerDouglasPeucker(points.subList(0, index + 1), epsilon)
        val secondHalf = ramerDouglasPeucker(points.subList(index, points.size), epsilon)

        return firstHalf.dropLast(1) + secondHalf
    }
}

