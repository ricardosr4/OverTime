package com.example.overtime.ui.screen.configuration

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(navController: NavController,
                 viewModel: ConfigViewModel = viewModel()) {

    Scaffold(
        topBar = {
            // Barra superior con el icono de cerrar sesión
            TopAppBar(
                title = { Text(text = "Configuración") },
                actions = {
                    IconButton(onClick = { viewModel.signOut(navController) }) {
                        Icon(
                            imageVector = Icons.Filled.ExitToApp, // Icono de cerrar sesión
                            contentDescription = "Cerrar sesión",
                            tint = Color.Black // Cambiar color del icono si es necesario
                        )
                    }
                }
            )
        },
        content = {
            ContentConfiguration()
        }
    )
}
@Composable
fun ContentConfiguration() {
    Column(modifier = Modifier) {

        Text("Configuración de usuario")

        Button(onClick = { /* Acción de configuración */ }) {
            Text("Guardar cambios")
        }
    }
}
