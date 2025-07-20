package com.example.overtime.presentation.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.overtime.data.model.WorkDay
import com.example.overtime.ui.component.ZetaAlertDialog
import com.example.overtime.ui.theme.CardColor

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
        border = BorderStroke(1.dp, Color.Black),
        colors = CardDefaults.cardColors(CardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = workDay.weekDay, fontSize = 16.sp, color = Color.Black)
                Text(text = "Horas extras: ${workDay.quantityOverHours}", fontSize = 14.sp, color = Color.Black)
                Text(text = "Porcentaje: ${workDay.percentageOverHours}%", fontSize = 14.sp, color = Color.Black)
            }
            IconButton(onClick = { showDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color.Gray
                )
            }
        }
    }
} 