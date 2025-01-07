package com.example.overtime.ui.login.state

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isFormValid: Boolean = false,
    val passwordVisualTransformation: VisualTransformation = PasswordVisualTransformation()
)
