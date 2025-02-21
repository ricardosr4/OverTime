package com.example.overtime.ui.login.viewModel

import android.util.Log
import android.util.Patterns
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.overtime.ui.login.state.LoginState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _loginState: MutableState<LoginState> = mutableStateOf(LoginState())
    val loginState: State<LoginState> get() = _loginState

    fun onEmailChanged(newEmail: String) {
        _loginState.value = _loginState.value.copy(email = newEmail)
        validateForm()
    }

    fun onPasswordChanged(newPassword: String) {
        _loginState.value = _loginState.value.copy(password = newPassword)
        validateForm()
    }

    fun onPasswordVisibilityChanged() {
        val newVisibility = !_loginState.value.isPasswordVisible
        _loginState.value = _loginState.value.copy(
            isPasswordVisible = newVisibility,
            passwordVisualTransformation = if (newVisibility) VisualTransformation.None else PasswordVisualTransformation()
        )
    }

    private fun validateForm() {
        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(_loginState.value.email).matches()
        val isValid = isEmailValid && _loginState.value.password.isNotEmpty() && _loginState.value.password.length >= 6

        _loginState.value = _loginState.value.copy(
            isFormValid = isValid
        )
    }

    fun login() {
        if (!_loginState.value.isFormValid) return

        // Indicar que se ha intentado hacer login
        _loginState.value = _loginState.value.copy(isLoginAttempted = true)

        viewModelScope.launch {
            auth.signInWithEmailAndPassword(
                _loginState.value.email,
                _loginState.value.password
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginState.value =
                        _loginState.value.copy(isSuccess = true, isLoginAttempted = false)
                } else {
                    _loginState.value = _loginState.value.copy(
                        errorMessage = task.exception?.message ?: "Error desconocido",
                        isLoginAttempted = false // Resetear el intento
                    )
                }
            }
        }
    }
    fun sendPasswordResetEmail() {
    }

}

