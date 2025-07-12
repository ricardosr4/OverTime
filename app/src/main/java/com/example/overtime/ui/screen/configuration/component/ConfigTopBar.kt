package com.example.overtime.ui.screen.configuration.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.overtime.ui.screen.configuration.ConfigViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigTopBar(
    viewModel: ConfigViewModel,
    navController: NavController
) {
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
                onClick = { viewModel.signOut(navController) }
            ) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = "Cerrar sesión",
                    tint = Color.Black
                )
            }
        }
    )
} 