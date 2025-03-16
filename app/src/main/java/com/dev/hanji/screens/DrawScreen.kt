package com.dev.hanji.screens

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PathMeasure
import android.util.Log
import android.util.MutableInt
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.PathParser
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.hanji.DrawingAction
import com.dev.hanji.DrawingViewModel
import com.dev.hanji.R
import com.dev.hanji.kanjiPack.KanjiPackStateById
import org.xmlpull.v1.XmlPullParser
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.min


@SuppressLint("DiscouragedApi")
@Composable
fun DrawScreen(modifier: Modifier = Modifier, drawingViewModel: DrawingViewModel, packState: KanjiPackStateById?) {
    if(packState?.kanjiPackWithKanjiList == null) {
        Text("Loading . . .")
        return
    }

    val currentIndex = remember { mutableIntStateOf(0) }
    val currentKanji = packState.kanjiPackWithKanjiList.kanjiList.getOrNull(currentIndex.intValue)

//    val unicodeHex = packState.kanjiPackWithKanjiList.kanjiList.getOrNull(2)?.unicodeHex
    val unicodeHex = currentKanji?.unicodeHex
    Log.d("Unicode Hex", "$unicodeHex")
    val context = LocalContext.current
    val drawableId = context.resources.getIdentifier(unicodeHex, "drawable", context.packageName)
    if (drawableId == 0) {
        return
    }
    Log.d("ID", "$drawableId")

    val originalPath: List<List<Offset>> =
        extractPathData(LocalContext.current, drawableId, 5f)
    Log.d("PATHS", "$originalPath")

    var currentOriginalPath by remember { mutableStateOf(originalPath) }
    var initialIndex by remember { mutableIntStateOf(0) }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Canvas(
                Modifier
                    .size(109.dp * 2.2f)
                    .aspectRatio(1f)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray)
            ) {
                Log.d("size", "$size.minDimension")
                originalPath.forEach { pathOffsets ->
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
                        color = Color.White,
                        style = Stroke(width = 15f),
                        )
                }
            }
            Button(
                onClick = {
                    currentIndex.intValue = (currentIndex.intValue + 1) % packState.kanjiPackWithKanjiList.kanjiList.size
                    val newKanji = packState.kanjiPackWithKanjiList.kanjiList.getOrNull(currentIndex.intValue)
                    val newUnicodeHex = newKanji?.unicodeHex
                    val newDrawableId = context.resources.getIdentifier(newUnicodeHex, "drawable", context.packageName)
                    if (newDrawableId != 0) {
                        currentOriginalPath = extractPathData(context, newDrawableId, 5f)
                        initialIndex = 0
                    }
                }
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Kanji")
            }
            Button(
                onClick = {}
            ) {
                Icon(imageVector =  Icons.Filled.PlayArrow, contentDescription = "Play kanji animation drawing")
            }
            Text("Draw a kanji\nKun-Yomi: ${currentKanji?.readingsKun?.joinToString(", ")}\nOn-Yomi: ${currentKanji?.readingsOn?.joinToString(", ")}\nMeaning:${currentKanji?.meanings?.joinToString(", ")}", textAlign = TextAlign.Center)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            DrawingCanvas(
                onAction = drawingViewModel::onAction,
                viewModel = drawingViewModel,
                originalPath = currentOriginalPath,
                initialIndex = initialIndex
            )

        }


    }
}

@Composable
fun DrawingCanvas(
    viewModel: DrawingViewModel,
    onAction: (DrawingAction) -> Unit,
    modifier: Modifier = Modifier,
    originalPath: List<List<Offset>>,
    initialIndex: Int,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Log.d("ORIGINAL PATH", "$originalPath")

    var indexOriginalPath by remember { mutableIntStateOf(initialIndex) }
    val matchedPath = remember { mutableStateListOf<List<Offset>>() }

    Box {
        Canvas(
            modifier = modifier
                .size(109.dp * 3)
                .aspectRatio(1f)
                .padding(16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            onAction(DrawingAction.OnNewPathStart)
                        },
                        onDragEnd = {
                            val epsilon = 5f
                            val simplifiedPath: List<Offset> = if (state.currentPath?.path!!.isNotEmpty()) {
                                ramerDouglasPeucker(state.currentPath?.path!!, epsilon)
                            } else {
                                state.currentPath?.path!!
                            }
                            onAction(DrawingAction.OnPathEnd)
                            Log.d("Original Size", "${originalPath.size}")
                            if(indexOriginalPath != originalPath.size){
                                val isCorrect =
                                    compareWithDTW(
                                        originalPath = originalPath[indexOriginalPath],
                                        currentPath = simplifiedPath
                                    )

                                if(isCorrect && originalPath[indexOriginalPath] !in matchedPath) {
                                    matchedPath.add(originalPath[indexOriginalPath])
                                    indexOriginalPath++
                                }
                            }
                        },
                        onDrag = { change, _ ->
                            onAction(DrawingAction.OnDraw(change.position))
                        },
                        onDragCancel = {
                            onAction(DrawingAction.OnPathEnd)
                        }
                    )
                }
        ) {


            state.currentPath?.let {
                drawPath(
                    path = it.path,
                    color = it.color,

                    )
            }

            originalPath.forEach { pathOffsets ->
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
                    style = Stroke(width = 15f),
                    alpha = 0.2f

                )
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
                    style = Stroke(width = 15f)
                )
            }


        }

    }

}


private fun DrawScope.drawPath(
    path: List<Offset>,
    color: Color,
    thickness: Float = 5f
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

private fun extractPathData(context: Context, resId: Int, scale: Float = 7.5f): List<List<Offset>> {
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
        val pathMeasure = PathMeasure(path, false)
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
    return  value < 13
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

