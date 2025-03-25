package com.example.overtime.ui.configuration

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(navController: NavController) {

    // TopAppBar con el icono de cerrar sesión
    TopAppBar(
        title = { Text(text = "Configuración") },
        actions = {
            IconButton(onClick = { signOut(navController) }) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp, // Icono de cerrar sesión
                    contentDescription = "Cerrar sesión",
                    tint = Color.Black // Cambia el color del icono si lo necesitas
                )
            }
        }
    )
}


fun signOut(navController: NavController) {
    // Cerrar sesión usando FirebaseAuth
    val auth = FirebaseAuth.getInstance()
    try {
        auth.signOut()
        // Navegar a la pantalla de login
        navController.navigate("login_screen") {
            // Puedes añadir flags o acciones para evitar el regreso a la pantalla de configuración
            popUpTo("config_screen") { inclusive = true }
        }
    } catch (e: Exception) {
        Toast.makeText(navController.context, "Error al cerrar sesión: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
    }
}


//Formula para obtener horas extras
//        sueldo base $
//        dividido x 30
//        multiplicado x 7
//        dividido 44
//        multiplicado x 1.5 = hrs al 50%
//        multiplicado x 1.75 = hrs al 75%
//        multiplicado x 2.0 = hrs al 100%
//        multiplicado x 2.3 = hrs al 130%