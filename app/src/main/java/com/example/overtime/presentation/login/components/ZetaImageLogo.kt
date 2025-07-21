package com.example.overtime.presentation.login.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ZetaImageLogo(
    image: Painter,
    modifier: Modifier = Modifier,
    contentDescription: String = "",
    width: Dp = 100.dp,
    height: Dp = 100.dp,
    contentScale: ContentScale = ContentScale.Crop
) {
    Box(modifier = modifier) {
        Image(
            painter = image,
            contentDescription = contentDescription,
            modifier = Modifier
                .size(width, height),
            contentScale = contentScale
        )
    }
}