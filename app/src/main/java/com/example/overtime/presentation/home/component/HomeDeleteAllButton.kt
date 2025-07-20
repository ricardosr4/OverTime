package com.example.overtime.presentation.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.overtime.ui.component.ZetaAlertDialog
import com.example.overtime.ui.theme.ButtonPrimary

@Composable
fun HomeDeleteAllButton(
    onDeleteAll: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Button(
        onClick = { showDialog = true },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.Black),
        colors = ButtonDefaults.buttonColors(containerColor = ButtonPrimary)
    ) {
        Text("Eliminar todas las hrs Extras")
    }

    if (showDialog) {
        ZetaAlertDialog(
            title = "Alerta",
            message = "Estas seguro de eliminar todos los registros?",
            confirmText = "Aceptar",
            onConfirmClick = {
                onDeleteAll()
                showDialog = false
            },
            onDismissClick = { showDialog = false }
        )
    }
} 