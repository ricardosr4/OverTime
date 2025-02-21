package com.example.overtime.ui.login.state

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isFormValid: Boolean = false,
    val isSuccess: Boolean = false,  // Para saber si el login fue exitoso
    val errorMessage: String? = null, // Para mostrar errores
    val isLoginAttempted: Boolean = false, // Estado para saber si se intentó el login
    val passwordVisualTransformation: VisualTransformation = PasswordVisualTransformation()
)
