package com.example.overtime.presentation.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.overtime.ui.theme.ButtonPrimary
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
            border = BorderStroke(1.dp, Color.Black),
            colors = ButtonDefaults.buttonColors(containerColor = ButtonPrimary)
        ) {
            Text("Eliminar hrs Extras")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onDownloadPdf,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.Black),
            colors = ButtonDefaults.buttonColors(containerColor = ButtonPrimary)
        ) {
            Text("Descargar PDF de hrs")
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