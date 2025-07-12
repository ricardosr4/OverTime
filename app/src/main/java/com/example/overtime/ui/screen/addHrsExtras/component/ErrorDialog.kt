package com.example.overtime.ui.screen.addHrsExtras.component

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.example.overtime.ui.theme.*

@Composable
fun ErrorDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            containerColor = Surface,
            title = {
                Text(
                    text = "Error de Validación",
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            text = {
                Text(
                    text = "Por favor, asegúrate de seleccionar una fecha y las horas extras antes de continuar.",
                    color = OnSurface
                )
            },
            confirmButton = {
                TextButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = PrimaryColor
                    )
                ) {
                    Text("Entendido")
                }
            }
        )
    }
} 