package com.dev.hanji.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.ActivityNavigatorExtras
import coil3.compose.rememberAsyncImagePainter
import com.dev.hanji.R
import com.dev.hanji.data.DataStoreRepository
import com.dev.hanji.data.events.UserEvent
import com.dev.hanji.data.state.OnBoardState
import com.dev.hanji.data.state.OnBoardUserState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun OnBoardItem(page: OnBoardState, isLastPage: Boolean, state: OnBoardUserState, onEvent: (UserEvent) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if(page.imageRes != null) {
            Image(
                painter = painterResource(id = page.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .height(350.dp)
                    .width(350.dp)
                    .padding(bottom = 20.dp)
            )
        }
        Text(
            text = page.title, style = TextStyle(
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
        )
        Text(
            text = page.description,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.W400,
                textAlign = TextAlign.Center,
            )
        )

        if (isLastPage) {
            AvatarPicker(
                state = state,
                onEvent = onEvent
            )
            OutlinedTextField(
                value = state.username,
                onValueChange = { newValue -> onEvent(UserEvent.SetUsername(newValue)) },
                label = { Text("Enter your username") },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(0.8f)
            )
        }

    }
}


@Composable
fun AvatarPicker(modifier: Modifier = Modifier,
        state: OnBoardUserState, onEvent: (UserEvent) -> Unit
) {
    onEvent(UserEvent.LoadAvatar)

    val avatarUri = state.avatar?.let { Uri.parse(it) }

    var selectedImage by remember { mutableStateOf<Uri?>(avatarUri) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImage = uri
        CoroutineScope(Dispatchers.IO).launch {
            onEvent(UserEvent.UpdateAvatar(uri.toString()))
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
            AvatarItem(imageUri = selectedImage, size = 256)

        Button(onClick = {
            launcher.launch("image/*")
        }) {
            Text("Choose Avatar")
        }
    }


    
}