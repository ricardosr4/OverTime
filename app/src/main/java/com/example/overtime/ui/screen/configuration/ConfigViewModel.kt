package com.example.overtime.ui.screen.configuration

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
class ConfigViewModel : ViewModel() {

    // Estado de configuración (en una app real esto se guardaría en SharedPreferences)
    private var _notificationsEnabled = true
    private var _isDarkMode = false

    val notificationsEnabled: Boolean
        get() = _notificationsEnabled

    val isDarkMode: Boolean
        get() = _isDarkMode

    // Obtener información del usuario actual
    fun getCurrentUser(): Pair<String, String> {
        val auth = Firebase.auth
        val user = auth.currentUser
        return if (user != null) {
            val name = user.displayName ?: "Usuario"
            val email = user.email ?: "No disponible"
            Pair(name, email)
        } else {
            Pair("Usuario", "No disponible")
        }
    }

    // Toggle notificaciones
    fun toggleNotifications() {
        _notificationsEnabled = !_notificationsEnabled
        // Aquí se guardaría en SharedPreferences
    }

    // Toggle tema
    fun toggleTheme() {
        _isDarkMode = !_isDarkMode
        // Aquí se guardaría en SharedPreferences y se aplicaría el tema
    }

    // Cambiar idioma (para futuras implementaciones)
//    fun changeLanguage(language: String) {
//        // Implementar cambio de idioma
//    }

    fun signOut(navController: NavController) {
        val auth = Firebase.auth
        try {
            auth.signOut()
            navController.navigate("login_screen") {
                popUpTo("config_screen") { inclusive = true }
            }
        } catch (e: Exception) {
            Toast.makeText(
                navController.context,
                "Error al cerrar sesión: ${e.localizedMessage}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}