package com.example.overtime.presentation.configuration.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.overtime.ui.component.ZetaAlertDialog
import com.example.overtime.presentation.configuration.viewmodel.ConfigViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigTopBar(
    viewModel: ConfigViewModel,
    navController: NavController
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    TopAppBar(
        title = { 
            Text(
                text = "Configuración",
                color = Color.Black
            ) 
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        ),
        actions = {
            IconButton(
                onClick = { showLogoutDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = "Cerrar sesión",
                    tint = Color.Black
                )
            }
        }
    )

    if (showLogoutDialog) {
        ZetaAlertDialog(
            title = "Confirmar Cierre de Sesión",
            message = "¿Estás seguro de que deseas cerrar sesión?",
            confirmText = "Cerrar Sesión",
            onConfirmClick = {
                viewModel.signOut(navController)
                showLogoutDialog = false
            },
            onDismissClick = { showLogoutDialog = false }
        )
    }
} 