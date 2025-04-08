package com.example.overtime.ui.screen.configuration

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

class ConfigViewModel : ViewModel() {

    fun signOut(navController: NavController) {
        val auth = FirebaseAuth.getInstance()
        try {
            auth.signOut()
            navController.navigate("login_screen") {
                popUpTo("config_screen") { inclusive = true }
            }
        } catch (e: Exception) {
            Toast.makeText(navController.context, "Error al cerrar sesión: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }
}