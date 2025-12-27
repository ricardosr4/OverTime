package com.example.overtime.presentation.addHrsExtras.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.overtime.presentation.login.components.StandardButton

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
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    )
}
