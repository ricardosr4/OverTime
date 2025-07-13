package com.example.overtime.ui.screen.addHrsExtras.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.overtime.ui.component.StandardButton

@Composable
fun AddHrsExtrasButton(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    StandardButton(
        onClick = onAddClick,
        text = "Agregar Horas Extras",
        modifier = modifier,
        icon = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Agregar",
                modifier = Modifier.size(24.dp),
                tint = com.example.overtime.ui.theme.ButtonPrimaryText
            )
        }
    )
} 