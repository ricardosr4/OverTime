package com.example.overtime.presentation.addHrsExtras.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.overtime.presentation.addHrsExtras.state.AddHrsExtrasState
import com.example.overtime.data.model.WorkDay
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddHrsExtrasViewModel @Inject constructor() : ViewModel() {

    private val _state = mutableStateOf(AddHrsExtrasState())
    val state: State<AddHrsExtrasState> = _state

    fun onDateSelected(date: String) {
        Log.d("StateUpdate", "Actualizando fecha: $date")
        _state.value = _state.value.copy(selectedDate = date)
    }

    fun onPercentageSelected(percentage: Int) {
        Log.d("StateUpdate", "Actualizando porcentaje: $percentage")
        _state.value = _state.value.copy(selectedPercentage = percentage)
    }

    fun onHoursSelected(hours: Int) {
        Log.d("StateUpdate", "Actualizando horas: $hours")
        _state.value = _state.value.copy(selectedHours = hours)
    }

    fun onShowDatePicker(show: Boolean) {
        _state.value = _state.value.copy(showDatePicker = show)
    }

    fun onShowErrorDialog(show: Boolean) {
        _state.value = _state.value.copy(showErrorDialog = show)
    }

    fun resetState() {
        Log.d("StateReset", "Reseteando estado")
        _state.value = AddHrsExtrasState()
    }

    private fun isFutureDate(dateString: String): Boolean {
        return try {
            Log.d("DateValidation", "Validando fecha: $dateString")
            val formatter = DateTimeFormatter.ofPattern("EEEE dd/MM/yyyy", Locale.getDefault())
            val selectedDate = LocalDate.parse(dateString, formatter)
            val today = LocalDate.now()
            val isFuture = selectedDate.isAfter(today)
            Log.d("DateValidation", "Fecha seleccionada: $selectedDate, Hoy: $today, Es futura: $isFuture")
            isFuture
        } catch (e: Exception) {
            Log.e("DateValidation", "Error al parsear fecha: $dateString", e)
            // Si hay error al parsear la fecha, consideramos que es inválida
            false
        }
    }

    fun validateFields(): Boolean {
        val currentState = _state.value
        
        Log.d("Validation", "=== INICIO VALIDACIÓN ===")
        Log.d("Validation", "Estado actual:")
        Log.d("Validation", "  - Fecha: '${currentState.selectedDate}'")
        Log.d("Validation", "  - Horas: ${currentState.selectedHours}")
        Log.d("Validation", "  - Porcentaje: ${currentState.selectedPercentage}")
        
        // Validar fecha
        if (currentState.selectedDate == "Selecciona una fecha") {
            Log.d("Validation", "❌ Fecha no seleccionada")
            return false
        }
        Log.d("Validation", "✅ Fecha válida")
        
        // Validar que la fecha no sea futura
        if (isFutureDate(currentState.selectedDate)) {
            Log.d("Validation", "❌ Fecha es futura")
            return false
        }
        Log.d("Validation", "✅ Fecha no es futura")
        
        // Validar horas (debe ser de 1 a 12)
        if (currentState.selectedHours < 1 || currentState.selectedHours > 12) {
            Log.d("Validation", "❌ Horas inválidas: ${currentState.selectedHours} (debe ser 1-12)")
            return false
        }
        Log.d("Validation", "✅ Horas válidas")
        
        // Validar porcentaje (debe ser 50, 75, 100 o 130)
        val validPercentages = listOf(50, 75, 100, 130)
        if (!validPercentages.contains(currentState.selectedPercentage)) {
            Log.d("Validation", "❌ Porcentaje inválido: ${currentState.selectedPercentage} (debe ser 50, 75, 100 o 130)")
            return false
        }
        Log.d("Validation", "✅ Porcentaje válido")
        
        Log.d("Validation", "✅ Todos los campos son válidos")
        Log.d("Validation", "=== FIN VALIDACIÓN ===")
        return true
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
                        // Resetear el estado después de agregar exitosamente
                        resetState()
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firebase", "Error al agregar el día de trabajo", e)
                    }
            } else {
                Log.e("Firebase", "No se pudo obtener el userId del usuario.")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModel", "ViewModel destruido - reseteando estado")
        resetState()
    }
}
