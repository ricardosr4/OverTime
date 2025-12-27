package com.example.overtime.presentation.addHrsExtras.component

import androidx.compose.runtime.Composable
import com.example.overtime.presentation.login.components.ZetaAlertDialog

@Composable
fun ErrorDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        ZetaAlertDialog(
            title = "Campos Requeridos",
            message = "Por favor, asegúrate de completar todos los campos correctamente:\n\n" +
                    "• Seleccionar una fecha\n" +
                    "• Seleccionar horas extras\n" +
                    "• Seleccionar porcentaje",
            confirmText = "Entendido",
            onConfirmClick = onDismiss
        )
    }
}
