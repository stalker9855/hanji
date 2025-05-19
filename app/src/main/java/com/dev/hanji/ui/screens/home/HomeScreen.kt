package com.dev.hanji.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dev.hanji.R
import com.dev.hanji.User
import com.dev.hanji.components.KanjiProgressSlider
import com.dev.hanji.components.cardStyle
import com.dev.hanji.data.dao.KanjiWithAttemptStatus
import com.dev.hanji.data.model.DailyAttempt
import com.dev.hanji.data.state.DailyState
import com.dev.hanji.data.viewmodel.ProgressViewModel
import com.dev.hanji.data.viewmodel.formatDisplayDate
import io.github.dautovicharis.charts.BarChart
import io.github.dautovicharis.charts.model.toChartDataSet
import io.github.dautovicharis.charts.style.BarChartDefaults
import io.github.dautovicharis.charts.style.ChartViewDefaults


@Composable
fun HomeScreen(modifier: Modifier = Modifier,
               progressViewModel: ProgressViewModel,
               dailyState: DailyState,
//               kanjiPackViewModel: KanjiPackViewModel,
               navController: NavController) {
   val kanjiProgress: LazyPagingItems<KanjiWithAttemptStatus> = progressViewModel.progress.collectAsLazyPagingItems()
   val attempts by progressViewModel.progressState.collectAsStateWithLifecycle()

   Column(modifier = modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
       Box(modifier = Modifier.cardStyle()) {
           Image(painter = painterResource(id = R.drawable.home), contentDescription = "")
       }
       Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 12.dp)) {
           Text(text = "Hanaji", fontWeight = FontWeight.SemiBold, fontSize = 48.sp, textAlign = TextAlign.Center)
       }
       Box(
           modifier = Modifier.clickable { navController.navigate("${User.route}") }
       ) {
           KanjiProgressSlider(
               kanjiProgress = kanjiProgress, attempts = attempts
           )
       }


       if(dailyState.dailyAttempts.size > 2) {
           AddDefaultBarChart(dailyAttempts =  dailyState.dailyAttempts)
       }
   }
}

@Composable
private fun AddDefaultBarChart(dailyAttempts: List<DailyAttempt>) {
    val values = dailyAttempts.map { it.attempts }
    val labels = dailyAttempts.map { formatDisplayDate(it.date) }

    val dataSet = values.toChartDataSet(
        title = "Daily Progress",
        prefix = "",
        labels = labels
    )

    BarChart(
        dataSet = dataSet,
        style = BarChartDefaults.style(
            chartViewStyle = ChartViewDefaults.style(
                outerPadding = 0.dp,
                innerPadding = 10.dp,
                backgroundColor = MaterialTheme.colorScheme.surfaceContainer
            )
        )
    )

}