package com.example.overtime.presentation.configuration.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import com.example.overtime.core.prefs.PreferencesManager
import com.example.overtime.core.prefs.ThemeMode
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    // Theme configuration
    val themeModeFlow: StateFlow<ThemeMode> = preferencesManager.themeModeFlow

    // Notifications configuration (pending implementation)
    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // User information
    private val _userInfo = MutableStateFlow(Pair("Usuario", "No disponible"))
    val userInfo: StateFlow<Pair<String, String>> = _userInfo.asStateFlow()

    // Theme management
    fun setThemeMode(mode: ThemeMode) {
        preferencesManager.setThemeMode(mode)
    }

    fun toggleTheme() {
        val currentMode = preferencesManager.getThemeMode()
        val nextMode = when (currentMode) {
            ThemeMode.DARK -> ThemeMode.LIGHT
            ThemeMode.LIGHT -> ThemeMode.DARK
            ThemeMode.SYSTEM -> ThemeMode.DARK
        }
        preferencesManager.setThemeMode(nextMode)
    }

    // Notifications management (pending implementation)
    fun toggleNotifications() {
        _notificationsEnabled.value = !_notificationsEnabled.value
        // TODO: Implement persistence
    }

    // User information management
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

    fun updateUserInfo(name: String, email: String) {
        _userInfo.value = Pair(name, email)
    }

    // Loading state management
    fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }

    // Sign out functionality (RESTAURADA)
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