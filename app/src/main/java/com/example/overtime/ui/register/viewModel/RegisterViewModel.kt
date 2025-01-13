package com.example.overtime.ui.register.viewModel

import android.util.Patterns
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.overtime.ui.register.state.RegisterState
import androidx.compose.runtime.State
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

class RegisterViewModel :ViewModel(){

    private val _registerState: MutableState<RegisterState> = mutableStateOf(RegisterState())
    val registerState:State<RegisterState> get() = _registerState

    fun onEmailChanged(newEmail: String) {
        _registerState.value = _registerState.value.copy(email = newEmail)
        validateForm()

    }

    fun onPasswordChanged(newPassword: String) {
        _registerState.value = _registerState.value.copy(password = newPassword)
        validateForm()

    }
    fun onPasswordConfirmationChanged(newConfirmationPassword: String) {
        _registerState.value = _registerState.value.copy(passwordConfirmation = newConfirmationPassword)
        validateForm()

    }
    fun onPasswordVisibilityChanged() {
        val newVisibility = !_registerState.value.isPasswordVisible
        _registerState.value = _registerState.value.copy(
            isPasswordVisible = newVisibility,
            passwordVisualTransformation = if (newVisibility) VisualTransformation.None else PasswordVisualTransformation()
        )
    }
    fun onPasswordConfirmationVisibilityChanged() {
        val newVisibility = !_registerState.value.isPasswordConfirmationVisible
        _registerState.value = _registerState.value.copy(
            isPasswordConfirmationVisible = newVisibility,
            passwordConfirmationVisualTransformation = if (newVisibility) VisualTransformation.None else PasswordVisualTransformation()
        )
    }
    private fun validateForm() {
        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(_registerState.value.email).matches()
        val isPasswordValid = _registerState.value.password.isNotEmpty()
        val isPasswordConfirmationValid =
            _registerState.value.password == _registerState.value.passwordConfirmation

        val isFormValid = isEmailValid && isPasswordValid && isPasswordConfirmationValid

        _registerState.value = _registerState.value.copy(
            isFormValid = isFormValid
        )
    }
}