package com.example.overtime.presentation.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.overtime.presentation.login.components.ZetaAlertDialog

@Composable
fun HomeActionsRow(
    onDeleteAll: () -> Unit,
    onDownloadPdf: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Eliminar Hrs", color = MaterialTheme.colorScheme.onPrimary)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onDownloadPdf,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Descargar PDF", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
    if (showDialog) {
        ZetaAlertDialog(
            title = "Alerta",
            message = "¿Estás seguro de eliminar todos los registros?",
            confirmText = "Aceptar",
            onConfirmClick = {
                onDeleteAll()
                showDialog = false
            },
            onDismissClick = { showDialog = false }
        )
    }
}