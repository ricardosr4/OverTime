package com.example.overtime.presentation.register.viewModel

import android.util.Patterns
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.overtime.domain.useCase.auth.RegisterUserUseCase
import com.example.overtime.presentation.register.state.AlertTypeRegister
import com.example.overtime.presentation.register.state.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _registerState: MutableState<RegisterState> = mutableStateOf(RegisterState())
    val registerState: State<RegisterState> get() = _registerState

    fun onNameChanged(newName: String) {
        _registerState.value = _registerState.value.copy(name = newName)
    }

    fun onEmailChanged(newEmail: String) {
        _registerState.value = _registerState.value.copy(email = newEmail)
    }

    fun onPasswordChanged(newPassword: String) {
        _registerState.value = _registerState.value.copy(password = newPassword)
    }

    fun onPasswordVisibilityChanged() {
        val newVisibility = !_registerState.value.isPasswordVisible
        _registerState.value = _registerState.value.copy(
            isPasswordVisible = newVisibility,
            passwordVisualTransformation = if (newVisibility) VisualTransformation.None else PasswordVisualTransformation()
        )
    }

    private fun validateInput(email: String, password: String): AlertTypeRegister? {
        return when {
            email.isEmpty() || password.isEmpty() -> AlertTypeRegister.EmptyField
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> AlertTypeRegister.InvalidEmail
            password.length < 6 -> AlertTypeRegister.InvalidPassword
            else -> null
        }
    }

    fun clearMessages() {
        _registerState.value = _registerState.value.copy(
            isSuccess = false,
            errorMessage = null
        )
    }

    fun closeAlert() {
        _registerState.value = registerState.value.copy(showAlert = false)
    }

    fun createUser(onSuccess: () -> Unit) {
        val email = registerState.value.email
        val password = registerState.value.password
        val name = registerState.value.name

        validateInput(email, password)?.let { errorType ->
            _registerState.value = _registerState.value.copy(
                showAlert = true,
                errorType = errorType,
                isLoading = false
            )
            return
        }
        _registerState.value = _registerState.value.copy(isLoading = true)
        viewModelScope.launch {
            val result = registerUserUseCase(name, email, password)
            if (result.isSuccess) {
                _registerState.value = RegisterState(isSuccess = true, isLoading = false)
                onSuccess()
                // cleanFields() eliminado para que isSuccess no se resetee antes de tiempo
            } else {
                _registerState.value = _registerState.value.copy(
                    showAlert = true,
                    errorType = AlertTypeRegister.UnknownError(result.exceptionOrNull()?.message ?: "Error desconocido"),
                    isLoading = false
                )
            }
        }
    }
}
