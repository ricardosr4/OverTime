package com.example.overtime.ui.login.viewModel

import android.util.Patterns
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.overtime.ui.login.state.LoginState
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

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
        val isValid = isEmailValid && _loginState.value.password.isNotEmpty()

        _loginState.value = _loginState.value.copy(
            isFormValid = isValid
        )
    }

    fun login() {
        viewModelScope.launch {
            // Lógica de login aquí
        }
    }
}

