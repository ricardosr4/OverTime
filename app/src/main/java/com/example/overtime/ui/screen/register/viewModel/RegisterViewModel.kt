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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _registerState: MutableState<RegisterState> = mutableStateOf(RegisterState())
    val registerState: State<RegisterState> get() = _registerState

    fun onEmailChanged(newEmail: String) {
        _registerState.value = _registerState.value.copy(email = newEmail)
        validateFields()

    }

    fun onPasswordChanged(newPassword: String) {
        _registerState.value = _registerState.value.copy(password = newPassword)
        validateFields()

    }

    fun onPasswordConfirmationChanged(newConfirmationPassword: String) {
        _registerState.value = _registerState.value.copy(
            passwordConfirmation = newConfirmationPassword)
        validateFields()

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

    private fun validateFields(): Boolean {
        val email = registerState.value.email
        val password = registerState.value.password
        val passwordConfirmation = registerState.value.passwordConfirmation

        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.isNotEmpty()
        val isPasswordConfirmationValid = password == passwordConfirmation

        val isFormValid = isEmailValid && isPasswordValid && isPasswordConfirmationValid

        _registerState.value = _registerState.value.copy(
            isFormValid = isFormValid
        )

        return isFormValid
    }

    fun createUser(onSuccess: () -> Unit) {
        val email = registerState.value.email
        val password = registerState.value.password

        if (validateFields()) {
            viewModelScope.launch {
                try {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                saveUser(email)
                                onSuccess()
                                cleanFields()
                            } else {
                                Log.d(
                                    "FirebaseAuth",
                                    "Error al crear usuario: ${task.exception?.message}"
                                )
                                // Manejar el error de creación de usuario
                            }
                        }
                } catch (e: Exception) {
                    Log.d("FirebaseAuth", "Error al crear usuario: ${e.localizedMessage}")
                    // Manejar otros errores
                }
            }
        } else {
            Log.d("Validation", "Campos inválidos o contraseña no coincide.")
            // Manejar el error de validación
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