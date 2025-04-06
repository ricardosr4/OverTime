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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _registerState: MutableState<RegisterState> = mutableStateOf(RegisterState())
    val registerState: State<RegisterState> get() = _registerState

    // Funciones de manejo de cambios en los campos
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

    // Función de validación de campos
    private fun validateInput(email: String, password: String): AlertTypeRegister? {
        return when {
            email.isEmpty() || password.isEmpty() -> AlertTypeRegister.EmptyField
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> AlertTypeRegister.InvalidEmail
            password.length < 6 -> AlertTypeRegister.InvalidPassword
            else -> null
        }
    }

    // Limpiar mensajes de estado
    fun clearMessages() {
        _registerState.value = _registerState.value.copy(
            isSuccess = false,
            errorMessage = null
        )
    }

    // Cerrar la alerta
    fun closeAlert() {
        _registerState.value = registerState.value.copy(showAlert = false)
    }

    // Función principal para crear el usuario
    fun createUser(onSuccess: () -> Unit) {
        val email = registerState.value.email
        val password = registerState.value.password

        // Validar los campos de entrada
        validateInput(email, password)?.let { errorType ->
            _registerState.value = _registerState.value.copy(
                showAlert = true,
                errorType = errorType
            )
            return  // Detener la ejecución si hay un error
        }

        // Si todas las validaciones pasan, intentamos crear el usuario
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            saveUser(email)
                            onSuccess()
                            cleanFields()
                        } else {
                            // Mostrar un error desconocido si el registro falla
                            _registerState.value = _registerState.value.copy(
                                showAlert = true,
                                errorType = AlertTypeRegister.UnknownError(task.exception?.message ?: "Error desconocido")
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

    // Limpiar los campos después de la creación del usuario
    private fun cleanFields() {
        _registerState.value = RegisterState()
    }

    // Guardar el usuario en la base de datos
    private fun saveUser(userName: String) {
        val id = auth.currentUser?.uid
        val email = auth.currentUser?.email

        val user = UserModel(
            userId = id.toString(),
            email = email.toString(),
        )

        // Guardamos los datos del usuario en la colección "Users"
        val userRef = FirebaseFirestore.getInstance().collection("Users").document(id.toString())

        // Se guardan los datos de usuario
        userRef.set(user)
            .addOnSuccessListener {
                // Crear una subcolección vacía de workdays para este usuario
                val workdays = emptyList<Map<String, Any>>() // Puede ser vacío al principio
                userRef.update("workdays", workdays)
                    .addOnSuccessListener {
                        Log.d("FIREBASE", "Se guardó el usuario y se creó la subcolección workdays.")
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
