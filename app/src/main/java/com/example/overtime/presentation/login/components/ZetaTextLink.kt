package com.example.overtime.presentation.login.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ZetaTextLink(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = Color.Black,
    textLink: String,
    linkColor: Color = Color.Blue,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = textColor,
            )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = textLink,
            color = linkColor,
            modifier = modifier.clickable(onClick = onClick)
        )
    }
}