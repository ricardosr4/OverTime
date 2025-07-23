package com.example.overtime.presentation.login.viewModel

import android.util.Log
import android.util.Patterns
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.overtime.presentation.login.state.AlertType
import com.example.overtime.presentation.login.state.LoginState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import com.example.overtime.domain.useCase.auth.LoginUserUseCase
import com.example.overtime.domain.useCase.auth.ResetPasswordUseCase
import com.example.overtime.domain.useCase.auth.LoginWithGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase
) : ViewModel() {

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

    fun clearMessages() {
        _loginState.value = _loginState.value.copy(
            isSuccess = false,
            errorMessage = null
        )
    }
    fun closeAlert() {
        _loginState.value = loginState.value.copy(showAlert = false)
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        if (email.isEmpty() || password.isEmpty()) {
            _loginState.value = _loginState.value.copy(
                showAlert = true,
                errorType = AlertType.EmptyField
            )
            return
        }
        _loginState.value = _loginState.value.copy(isLoading = true)
        viewModelScope.launch {
            val result = loginUserUseCase(email, password)
            if (result.isSuccess) {
                _loginState.value = _loginState.value.copy(
                    isSuccess = true,
                    errorType = null,
                    isLoading = false
                )
                onSuccess()
            } else {
                _loginState.value = _loginState.value.copy(
                    showAlert = true,
                    errorType = AlertType.InvalidCredentials,
                    isLoading = false
                )
            }
        }
    }

    fun resetPassword(email: String, onSuccess: () -> Unit) {
        if (email.isNotEmpty()) {
            _loginState.value = _loginState.value.copy(isLoading = true)
            viewModelScope.launch {
                val result = resetPasswordUseCase(email)
                if (result.isSuccess) {
                    _loginState.value = _loginState.value.copy(
                        showAlert = true,
                        errorType = AlertType.ResetPasswordSuccess,
                        isLoading = false
                    )
                    onSuccess()
                } else {
                    _loginState.value = _loginState.value.copy(
                        showAlert = true,
                        errorType = AlertType.ResetPasswordInvalidEmail,
                        isLoading = false
                    )
                }
            }
        } else {
            _loginState.value = _loginState.value.copy(
                showAlert = true,
                errorType = AlertType.ResetPasswordEmptyField,
                isLoading = false
            )
        }
    }

    fun loginWithGoogle(idToken: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        _loginState.value = _loginState.value.copy(isLoading = true)
        viewModelScope.launch {
            val result = loginWithGoogleUseCase(idToken)
            if (result.isSuccess) {
                _loginState.value = _loginState.value.copy(
                    isSuccess = true,
                    errorType = null,
                    isLoading = false
                )
                onSuccess()
            } else {
                _loginState.value = _loginState.value.copy(
                    showAlert = true,
                    errorType = AlertType.InvalidCredentials,
                    isLoading = false
                )
                onError(result.exceptionOrNull()?.localizedMessage ?: "Error desconocido")
            }
        }
    }
}
