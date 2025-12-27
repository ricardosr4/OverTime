package com.example.overtime.presentation.configuration.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.overtime.core.prefs.PreferencesManager
import com.example.overtime.core.prefs.ThemeMode
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    // Tema
    val themeModeFlow: StateFlow<ThemeMode> = preferencesManager.themeModeFlow

    // Día de cierre de mes
    val monthClosingDayFlow: StateFlow<Int> = preferencesManager.monthClosingDayFlow

    // Loading global
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Información de usuario
    private val _userInfo = MutableStateFlow(Pair("Usuario", "No disponible"))
    val userInfo: StateFlow<Pair<String, String>> = _userInfo.asStateFlow()

    fun toggleTheme() {
        val current = preferencesManager.getThemeMode()
        val next = when (current) {
            ThemeMode.DARK -> ThemeMode.LIGHT
            ThemeMode.LIGHT -> ThemeMode.DARK
            ThemeMode.SYSTEM -> ThemeMode.DARK
        }
        preferencesManager.setThemeMode(next)
    }

    fun setMonthClosingDay(day: Int) {
        preferencesManager.setMonthClosingDay(day)
    }

    // -------- Usuario (Firestore / Auth) --------
    fun getCurrentUser(): Pair<String, String> {
        val auth = Firebase.auth
        val user = auth.currentUser

        if (user != null) {
            val userId = user.uid
            val email = user.email ?: "No disponible"

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

    fun signOut(navController: NavController, context: Context) {
        val auth = Firebase.auth
        try {
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
                navController.navigate("login_screen")
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
