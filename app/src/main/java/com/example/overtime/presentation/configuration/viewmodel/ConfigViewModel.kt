package com.example.overtime.presentation.configuration.viewmodel

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import android.content.Context

class ConfigViewModel : ViewModel() {

    // Estado de configuración (en una app real esto se guardaría en SharedPreferences)
    private var _notificationsEnabled = true
    private var _isDarkMode = false

    val notificationsEnabled: Boolean
        get() = _notificationsEnabled

    val isDarkMode: Boolean
        get() = _isDarkMode

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Estado para la información del usuario
    private val _userInfo = MutableStateFlow(Pair("Usuario", "No disponible"))
    val userInfo: StateFlow<Pair<String, String>> = _userInfo.asStateFlow()

    // Obtener información del usuario actual desde Firestore
    fun getCurrentUser(): Pair<String, String> {
        val auth = Firebase.auth
        val user = auth.currentUser

        if (user != null) {
            val userId = user.uid
            val email = user.email ?: "No disponible"

            // Obtener el nombre desde Firestore
            Firebase.firestore.collection("Users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val userName = document.getString("userName") ?: "Usuario"
                        _userInfo.value = Pair(userName, email)
                    } else {
                        _userInfo.value = Pair("Usuario", email)
                    }
                }
                .addOnFailureListener {
                    _userInfo.value = Pair("Usuario", email)
                }

            return _userInfo.value
        } else {
            return Pair("Usuario", "No disponible")
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

    fun signOut(navController: NavController, context: Context) {
        val auth = Firebase.auth
        try {
            navController.navigate("splash_screen")
            viewModelScope.launch {
                _isLoading.value = true
                auth.signOut()
                // Cerrar sesión de Google también
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("782417725602-045du8t2rpeh6t9sv6otmpt063rg3va7.apps.googleusercontent.com")
                    .requestEmail()
                    .build()
                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                googleSignInClient.signOut()
                delay(1000)
                _isLoading.value = false
                navController.navigate("login_screen") {
                    popUpTo("splash_screen") { inclusive = true }
                }
            }
        } catch (e: Exception) {
            _isLoading.value = false
            Toast.makeText(
                navController.context,
                "Error al cerrar sesión: ${e.localizedMessage}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}