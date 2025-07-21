package com.example.overtime.presentation.login.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun ZetaText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp,
    color: Color = Color.Black,
    maxLines: Int = Int.MAX_VALUE,

) {
    Text(
        text = text,
        fontSize = fontSize,
        color = color,
        modifier = modifier,
        maxLines = maxLines,
    )
}