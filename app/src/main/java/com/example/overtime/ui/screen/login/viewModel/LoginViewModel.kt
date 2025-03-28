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

    private fun validateFields(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        if (validateFields(email, password)) {
            viewModelScope.launch {
                try {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                onSuccess()

                            } else {
                                Log.d("ERROR EN FIREBASE", "Usuario y/o contraseña incorrectos")
                                //mostrar toas o alertDialog
                            }
                        }
                } catch (e: Exception) {
                    Log.d("ERROR EN FIREBASE", "Error: ${e.localizedMessage}")
                }
            }
        } else {
            Log.d("VALIDACION", "Los campos no son válidos o están vacíos.")
            //mostrar toas o alertDialog

        }
    }
    // Función para recuperar la contraseña
    fun resetPassword(email: String, onSuccess: () -> Unit) {
        if (email.isNotEmpty()) {
            viewModelScope.launch {
                try {
                    auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("RECUPERACIÓN", "Correo enviado para recuperar la contraseña.")
                                onSuccess()
                            } else {
                                Log.d("RECUPERACIÓN", "Error al enviar el correo de recuperación.")
                                //mostrar toas o alertDialog
                            }
                        }
                } catch (e: Exception) {
                    Log.d("ERROR EN FIREBASE", "Error: ${e.localizedMessage}")
                }
            }
        } else {
            Log.d("VALIDACION", "El campo de correo está vacío.")
            //mostrar toas o alertDialog
        }
    }

}





//    fun login() {
//        if (!_loginState.value.isFormValid) return
//
//        // Indicar que se ha intentado hacer login
//        _loginState.value = _loginState.value.copy(isLoginAttempted = true)
//
//        viewModelScope.launch {
//            auth.signInWithEmailAndPassword(
//                _loginState.value.email,
//                _loginState.value.password
//            ).addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    _loginState.value =
//                        _loginState.value.copy(isSuccess = true, isLoginAttempted = false)
//                } else {
//                    _loginState.value = _loginState.value.copy(
//                        errorMessage = task.exception?.message ?: "Error desconocido",
//                        isLoginAttempted = false // Resetear el intento
//                    )
//                }
//            }
//        }
//    }


