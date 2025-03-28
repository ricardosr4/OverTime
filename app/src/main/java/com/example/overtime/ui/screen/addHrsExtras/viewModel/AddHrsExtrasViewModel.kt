package com.example.overtime.ui.screen.addHrsExtras.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.overtime.ui.screen.addHrsExtras.state.AddHrsExtrasState
import com.example.overtime.data.model.WorkDay
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AddHrsExtrasViewModel : ViewModel() {

    private val _state = mutableStateOf(AddHrsExtrasState())
    val state = _state

    // Funciones para manejar los cambios en la UI
    fun onDateSelected(date: String) {
        _state.value = _state.value.copy(selectedDate = date)
    }

    fun onPercentageSelected(percentage: Int) {
        _state.value = _state.value.copy(selectedPercentage = percentage)
    }

    fun onHoursSelected(hours: Int) {
        _state.value = _state.value.copy(selectedHours = hours)
    }

    fun onShowDatePicker(show: Boolean) {
        _state.value = _state.value.copy(showDatePicker = show)
    }

    fun onShowErrorDialog(show: Boolean) {
        _state.value = _state.value.copy(showErrorDialog = show)
    }

    // Función para agregar el WorkDay a Firestore
    fun addWorkDay(workDay: WorkDay) {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid // Obtener el userId del usuario autenticado

            if (userId != null) {
                // Obtenemos la instancia de Firestore
                val db = FirebaseFirestore.getInstance()

                // Referencia al documento del usuario en la colección "Users"
                val userRef = db.collection("Users").document(userId)

                // Agregar el WorkDay a la subcolección "workdays"
                userRef.collection("workdays")
                    .add(workDay)
                    .addOnSuccessListener { documentReference ->
                        Log.d("Firebase", "Día de trabajo agregado con ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firebase", "Error al agregar el día de trabajo", e)
                    }
            } else {
                Log.e("Firebase", "No se pudo obtener el userId del usuario.")
            }
        }
    }
}
