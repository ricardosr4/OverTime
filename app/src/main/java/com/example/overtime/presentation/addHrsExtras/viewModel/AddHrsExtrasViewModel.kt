package com.example.overtime.presentation.addHrsExtras.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.overtime.presentation.addHrsExtras.state.AddHrsExtrasState
import com.example.overtime.data.model.WorkDay
import com.example.overtime.domain.useCase.workday.AddWorkDayUseCase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddHrsExtrasViewModel @Inject constructor(
    private val addWorkDayUseCase: AddWorkDayUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

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
        if (currentState.selectedDate == "Selecciona una fecha") {
            Log.d("Validation", "❌ Fecha no seleccionada")
            return false
        }
        Log.d("Validation", "✅ Fecha válida")
        if (isFutureDate(currentState.selectedDate)) {
            Log.d("Validation", "❌ Fecha es futura")
            return false
        }
        Log.d("Validation", "✅ Fecha no es futura")
        if (currentState.selectedHours < 1 || currentState.selectedHours > 12) {
            Log.d("Validation", "❌ Horas inválidas: ${currentState.selectedHours} (debe ser 1-12)")
            return false
        }
        Log.d("Validation", "✅ Horas válidas")
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
            val userId = firebaseAuth.currentUser?.uid
            if (userId != null) {
                val result = addWorkDayUseCase(userId, workDay)
                if (result.isSuccess) {
                    Log.d("WorkDay", "Día de trabajo agregado correctamente")
                    resetState()
                } else {
                    Log.w("WorkDay", "Error al agregar el día de trabajo: ${result.exceptionOrNull()?.message}")
                }
            } else {
                Log.e("WorkDay", "No se pudo obtener el userId del usuario.")
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModel", "ViewModel destruido - reseteando estado")
        resetState()
    }
}
