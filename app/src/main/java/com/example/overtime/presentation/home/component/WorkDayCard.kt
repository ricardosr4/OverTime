package com.example.overtime.presentation.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.overtime.data.model.WorkDay
import com.example.overtime.presentation.login.components.ZetaAlertDialog

@Composable
fun WorkDayCard(
    workDay: WorkDay,
    onDeleteConfirm: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        ZetaAlertDialog(
            title = "Confirmar eliminación",
            message = "¿Estás seguro de que deseas eliminar este ítem?",
            confirmText = "Eliminar",
            onConfirmClick = {
                onDeleteConfirm()
                showDialog = false
            },
            onDismissClick = { showDialog = false }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = workDay.weekDay, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                Text(text = "Horas extras: ${workDay.quantityOverHours}", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                Text(text = "Porcentaje: ${workDay.percentageOverHours}%", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
            }
            IconButton(onClick = { showDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
