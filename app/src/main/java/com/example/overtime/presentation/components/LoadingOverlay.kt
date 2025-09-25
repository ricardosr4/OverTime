package com.example.overtime.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.overtime.ui.theme.ButtonPrimary

@Composable
fun LoadingOverlay(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0x88000000)), // gris semitransparente
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = ButtonPrimary)
    }
} 