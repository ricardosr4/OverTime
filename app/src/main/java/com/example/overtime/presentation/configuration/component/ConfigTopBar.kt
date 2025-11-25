package com.example.overtime.presentation.configuration.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.overtime.presentation.login.components.ZetaAlertDialog
import com.example.overtime.presentation.configuration.viewmodel.ConfigViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigTopBar(
    viewModel: ConfigViewModel,
    navController: NavController
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    TopAppBar(
        title = { 
            Text(
                text = "Configuración",
                color = MaterialTheme.colorScheme.onSurface
            ) 
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        actions = {
            IconButton(
                onClick = { showLogoutDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = "Cerrar sesión",
                    tint = MaterialTheme.colorScheme.onSurface
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
                viewModel.signOut(navController, context)
                showLogoutDialog = false
            },
            onDismissClick = { showLogoutDialog = false }
        )
    }
} 