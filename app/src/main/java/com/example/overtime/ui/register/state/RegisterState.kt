package com.example.overtime.ui.register.state

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

data class RegisterState(
    val email: String = "",
    val password: String = "",
    val passwordConfirmation: String = "",

    val isPasswordVisible: Boolean = false,
    val isPasswordConfirmationVisible: Boolean = false,

    val isFormValid: Boolean = false,

    val passwordVisualTransformation: VisualTransformation = PasswordVisualTransformation(),
    val passwordConfirmationVisualTransformation: VisualTransformation = PasswordVisualTransformation()

)
