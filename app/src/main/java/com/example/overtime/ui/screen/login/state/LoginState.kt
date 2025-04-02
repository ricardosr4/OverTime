package com.example.overtime.ui.screen.login.state

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val passwordVisualTransformation: VisualTransformation = PasswordVisualTransformation(),

    val isFormValid: Boolean = false,
    val showAlert: Boolean = false,

    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val errorType: AlertType? = null,



    )
