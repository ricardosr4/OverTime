package com.example.overtime.presentation.register.state

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val passwordVisualTransformation: VisualTransformation = PasswordVisualTransformation(),

    val isFormValid: Boolean = false,
    val showAlert: Boolean = false,

    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val errorType: AlertTypeRegister? = null,
    val isLoading: Boolean = false

)
