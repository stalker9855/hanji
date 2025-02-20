package com.dev.hanji.screens

import android.content.Context
import android.graphics.PathMeasure
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.core.graphics.PathParser
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dev.hanji.DrawingAction
import com.dev.hanji.DrawingViewModel
import com.dev.hanji.PathData
import com.dev.hanji.R
import org.xmlpull.v1.XmlPullParser
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sqrt


@Composable
fun PacksScreen(modifier: Modifier = Modifier) {
    val viewModel = viewModel<DrawingViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        DrawingCanvas(
            paths = state.paths,
            currentPath = state.currentPath,
            onAction = viewModel::onAction,
            modifier =
                Modifier.fillMaxWidth()
        )

        Button(
            onClick = {viewModel.onAction(DrawingAction.OnClearCanvasClick) }
        ) {
            Text("Clear")
        }
    }

}

@Composable
fun DrawingCanvas(paths: List<PathData>,
                  currentPath: PathData?,
                  onAction: (DrawingAction) -> Unit,
                  modifier: Modifier = Modifier) {
    val vector = ImageVector.vectorResource(R.drawable.achievement1)
    val painter = rememberVectorPainter(image = vector)

    // DELETE AFTER ALL
    val viewModel = viewModel<DrawingViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val originalPath: List<List<Offset>> = extractPathData(LocalContext.current, R.drawable.achievement1)
    // DELETE
    Canvas(
       modifier = modifier
           .size(CANVAS_SIZE * CANVAS_SCALE)
           .background(Color.White)
           .pointerInput(true) {
               detectDragGestures(
                   onDragStart = {
                       onAction(DrawingAction.OnNewPathStart)

                   },
                   onDragEnd = {
                       onAction(DrawingAction.OnPathEnd)
                       Log.d("check dTW", "${state.currentPath?.let { compareWithDTW(originalPath = originalPath[0], currentPath = it.path) }}")
                   },
                   onDrag = { change, _  ->
                       onAction(DrawingAction.OnDraw(change.position))
                   },
                   onDragCancel = {
                       onAction(DrawingAction.OnPathEnd)
                   }
               )
           }

    ){
        with(painter) {
            draw(painter.intrinsicSize)
        }
        paths.fastForEach {
            pathData ->
            drawPath(
                path = pathData.path,
                color = pathData.color,
            )
        }
        currentPath?.let {
            drawPath(
                path =  it.path,
                color = it.color
            )
        }
    }
}

private fun DrawScope.drawPath(
    path: List<Offset>,
    color: Color,
    thickness: Float = 10f
) {
   val smoothedPath = Path().apply {
       if(path.isNotEmpty()) {
           moveTo(path.first().x, path.first().y)

           val smoothness = 5
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

private val CANVAS_SIZE: Dp = 109.dp
private const val CANVAS_SCALE: Int = 3

fun extractPathData(context: Context, resId: Int): List<List<Offset>> {
    val parser = context.resources.getXml(resId)
    val pathDataList = mutableListOf<String>()

    try {
        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            if (parser.eventType == XmlPullParser.START_TAG && parser.name == "path") {
                for (i in 1 until parser.attributeCount) {
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

    return parsePathData(pathDataList = pathDataList)
}

fun parsePathData(pathDataList: MutableList<String>, numPoints: Int = 20): List<List<Offset>> {
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
                points.add(Offset(pos[0], pos[1]))
            }
        }
        paths.add(points)
    }

    return paths
}


private fun compareWithDTW(originalPath: List<Offset>, currentPath: List<Offset>): Float {

    val matrix = Array(originalPath.count() + 1) {
        i ->
            Array(currentPath.count() + 1) {
                j ->
                when {
                    i == 0 && j == 0 -> 0f
                    i == 0 || j == 0 -> Float.MAX_VALUE - 100
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

    return matrix[originalPath.count()][currentPath.count()]
}

fun cost(p1: Offset, p2: Offset): Float = sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y))

fun min(a: Float, b: Float, c: Float): Float = min(a, min(b,c))

