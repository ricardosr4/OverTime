package com.example.overtime.ui.screen.configuration

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
            TopAppBar(
                title = { Text(text = "Configuración") },
                actions = {
                    IconButton(onClick = { viewModel.signOut(navController) }) {
                        Icon(
                            imageVector = Icons.Filled.ExitToApp,
                            contentDescription = "Cerrar sesión",
                            tint = Color.Black
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            ContentConfiguration(paddingValues)
        }
    )
}

@Composable
fun ContentConfiguration(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Text("Configuración de usuario")
        Button(modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { /* Acción de configuración */ }) {
            Text("Guardar cambios")
        }
    }
}
