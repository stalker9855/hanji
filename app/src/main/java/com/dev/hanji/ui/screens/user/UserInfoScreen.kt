package com.dev.hanji.ui.screens.user

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.hanji.R
import com.dev.hanji.UserStats
import com.dev.hanji.components.AttemptBox
import com.dev.hanji.components.AvatarItem
import com.dev.hanji.components.CircleStats
import com.dev.hanji.components.cardStyle
import com.dev.hanji.data.events.UserEvent
import com.dev.hanji.data.state.UserState
import com.dev.hanji.data.viewmodel.UserViewModel


@Composable
fun UserInfoScreen(modifier: Modifier = Modifier, state: UserState, onEvent: (UserEvent) -> Unit) {
    onEvent(UserEvent.LoadAvatar)
    val avatar = state.avatar?.let { Uri.parse(it) }
    Column {
        Column(modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
        )
        {
            Row(Modifier.cardStyle()
            ) {
                AvatarItem(imageUri = avatar)
                Column(modifier = Modifier.padding(16.dp).align(Alignment.CenterVertically)) {
                    state.user?.let {
                        Text(text= it.username,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    }
                    HorizontalDivider(modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                    )
                    Text("Level - Beginner")
                }
            }
            // Attempts
            Column {
                Column(modifier = Modifier.padding(vertical = 8.dp).cardStyle()) {
                    AttemptBox(attempts = state.attempts)
                }

                // Circle Stats
                state.attempts?.let { CircleStats(state = state) }


                // TEST bar Chart
                Spacer(modifier = Modifier.height(20.dp))

            }

        }
    }
}