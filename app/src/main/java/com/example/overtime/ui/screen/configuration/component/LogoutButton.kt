package com.example.overtime.ui.screen.configuration.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.overtime.ui.component.ZetaAlertDialog
import com.example.overtime.ui.theme.ButtonPrimary

@Composable
fun LogoutButton(
    onLogout: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Button(
        onClick = { showDialog = true },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.Black),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f))
    ) {
        Icon(
            imageVector = Icons.Default.Logout,
            contentDescription = "Cerrar sesión",
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Cerrar Sesión",
            fontSize = 16.sp,
            color = Color.White
        )
    }

    if (showDialog) {
        ZetaAlertDialog(
            title = "Confirmar Cierre de Sesión",
            message = "¿Estás seguro de que deseas cerrar sesión?",
            confirmText = "Cerrar Sesión",
            onConfirmClick = {
                onLogout()
                showDialog = false
            },
            onDismissClick = { showDialog = false }
        )
    }
} 