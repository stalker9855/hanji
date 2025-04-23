package com.dev.hanji.components

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.dev.hanji.R


@Composable
fun AvatarItem(modifier: Modifier = Modifier,
               id: Int = R.drawable.avatar4,
               imageUri: Uri? = null,
               size: Int = 128
) {
    val painter = if (imageUri != null) {
        rememberAsyncImagePainter(imageUri)
    } else {
        painterResource(id)
    }

    Log.d("AVATAR", "$painter")

    Image(painter = painter,
        contentDescription = "avatar", modifier = Modifier
            .padding(16.dp)
            .size(size.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondaryContainer)
    )
}