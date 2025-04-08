package com.example.overtime.ui.screen.login.viewModel

import android.util.Log
import android.util.Patterns
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.overtime.ui.screen.login.state.AlertType
import com.example.overtime.ui.screen.login.state.LoginState
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
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _loginState.value = _loginState.value.copy(
                                isSuccess = true,
                                errorType = null
                            )
                            onSuccess()
                        } else {
                            _loginState.value = _loginState.value.copy(
                                showAlert = true,
                                errorType = AlertType.InvalidCredentials
                            )
                        }
                    }
            } catch (e: Exception) {
                _loginState.value = _loginState.value.copy(
                    showAlert = true,
                    errorType = e.localizedMessage?.let { AlertType.UnknownError(it) }
                )
            }
        }
    }

    fun resetPassword(email: String, onSuccess: () -> Unit) {
        if (email.isNotEmpty()) {
            viewModelScope.launch {
                try {
                    auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                _loginState.value = _loginState.value.copy(
                                    showAlert = true,
                                    errorType = AlertType.ResetPasswordSuccess
                                )
                                onSuccess()
                            } else {
                                _loginState.value = _loginState.value.copy(
                                    showAlert = true,
                                    errorType = AlertType.ResetPasswordInvalidEmail
                                )
                                Log.d("RECUPERACIÓN", "Error al enviar el correo de recuperación.")
                            }
                        }
                } catch (e: Exception) {
                    Log.d("ERROR EN FIREBASE", "Error: ${e.localizedMessage}")
                }
            }
        } else {
            _loginState.value = _loginState.value.copy(
                showAlert = true,
                errorType = AlertType.ResetPasswordEmptyField
            )
        }
    }
}
