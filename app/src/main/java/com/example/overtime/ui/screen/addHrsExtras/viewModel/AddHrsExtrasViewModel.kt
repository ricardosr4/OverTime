package com.example.overtime.ui.screen.addHrsExtras.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.overtime.ui.screen.addHrsExtras.state.AddHrsExtrasState
import com.example.overtime.data.model.WorkDay
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class AddHrsExtrasViewModel : ViewModel() {

    private val _state = mutableStateOf(AddHrsExtrasState())
    val state: State<AddHrsExtrasState> = _state

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

    fun addWorkDay(workDay: WorkDay) {
        viewModelScope.launch {
            val userId = Firebase.auth.currentUser?.uid // Obtener el userId del usuario autenticado
            if (userId != null) {
                val db = Firebase.firestore
                val userRef = db.collection("Users").document(userId)
                userRef.collection("workdays")
                    .add(
                        WorkDay(
                            weekDay = workDay.weekDay,
                            quantityOverHours = workDay.quantityOverHours,
                            percentageOverHours = workDay.percentageOverHours
                        )
                    )
                    .addOnSuccessListener { documentReference ->
                        val docId = documentReference.id
                        documentReference.update("id", docId)
                        Log.d("Firebase", "Día de trabajo agregado con ID: $docId")
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
