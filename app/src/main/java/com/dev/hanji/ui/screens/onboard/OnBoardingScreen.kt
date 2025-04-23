package com.dev.hanji.ui.screens.onboard


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dev.hanji.Home
import com.dev.hanji.R
import com.dev.hanji.components.OnBoardItem
import com.dev.hanji.data.events.UserEvent
import com.dev.hanji.data.state.OnBoardState
import com.dev.hanji.data.state.OnBoardUserState
import kotlinx.coroutines.launch


@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    state: OnBoardUserState,
    onEvent: (UserEvent) -> Unit,
    onFinish: () -> Unit,
    navController: NavController,
) {
    val pages = listOf(
        OnBoardState(
            title = "ようかそ！",
            description = "Welcome to Hanaji!",
            imageRes = R.drawable.avatar4
        ),
        OnBoardState(
            title = "花字 (Hanaji)",
            description = "This is an application for learning how to draw kanji.\nEverything is in your hands!",
            imageRes = R.drawable.avatar4
        ),
        OnBoardState(
            title = "To continue, enter your username",
            description = "",
            imageRes = null
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            OnBoardItem(page = pages[page], isLastPage = (page == 2), onEvent = onEvent, state = state)
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)

        ) {

            Text(
                "Skip", style = TextStyle(
                    color = Color(0xFFAAAAAA),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                ),
                modifier = Modifier.clickable {
                    val skipPage = pagerState.pageCount-1
                    coroutineScope.launch { pagerState.animateScrollToPage(skipPage) }
                }
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                repeat(pages.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .width(if (isSelected) 18.dp else 8.dp)
                            .height(if (isSelected) 8.dp else 8.dp)
                            .border(
                                width = 1.dp,
                                color = Color(0xFF707784),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .background(
                                color = if (isSelected) Color(0xFF3B6C64) else Color(0xFFFFFFFF),
                                shape = CircleShape
                            )
                    )
                }
            }


            Text(
                if(pagerState.currentPage == 2) "Finish" else "Next", style = TextStyle(
//                    color = Color(0xFF333333),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                ),
                modifier = Modifier.clickable {
                    onEvent(UserEvent.InsertUser)
                    if (pagerState.currentPage < 2) {
                        val nextPage = pagerState.currentPage + 1
                        coroutineScope.launch { pagerState.animateScrollToPage(nextPage) }
                    }
                    if (pagerState.currentPage == 2) {
                        onFinish()
                    }

                }
            )

        }
    }
}
