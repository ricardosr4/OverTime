package com.example.overtime.ui.screen.register.viewModel

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.overtime.ui.screen.register.state.RegisterState
import androidx.compose.runtime.State
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.viewModelScope
import com.example.overtime.data.model.UserModel
import com.example.overtime.ui.screen.register.state.AlertTypeRegister
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val auth = Firebase.auth

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
                errorType = errorType
            )
            return
        }
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            saveUser(name)
                            onSuccess()
                            cleanFields()
                        } else {
                            _registerState.value = _registerState.value.copy(
                                showAlert = true,
                                errorType = AlertTypeRegister.UnknownError(
                                    task.exception?.message ?: "Error desconocido"
                                )
                            )
                        }
                    }
            } catch (e: Exception) {
                _registerState.value = _registerState.value.copy(
                    showAlert = true,
                    errorType = AlertTypeRegister.UnknownError(e.localizedMessage)
                )
            }
        }
    }

    private fun cleanFields() {
        _registerState.value = RegisterState()
    }

    private fun saveUser(userName: String) {
        val id = auth.currentUser?.uid
        val email = auth.currentUser?.email

        val user = UserModel(
            userId = id.toString(),
            email = email.toString(),
            userName = userName
        )

        val userRef = Firebase.firestore.collection("Users").document(id.toString())
        userRef.set(user.toMap())
            .addOnSuccessListener {
                val workdays = emptyList<Map<String, Any>>()
                userRef.update("workdays", workdays)
                    .addOnSuccessListener {
                        Log.d(
                            "FIREBASE",
                            "Se guardó el usuario y se creó la subcolección workdays."
                        )
                    }
                    .addOnFailureListener {
                        Log.d("FIREBASE", "No se pudo crear la subcolección workdays.")
                    }
            }
            .addOnFailureListener {
                Log.d("FIREBASE", "No se pudo guardar el usuario")
            }
    }
}
