package com.example.overtime.ui.addHrsExtras.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.overtime.ui.addHrsExtras.state.AddHrsExtrasState
import com.example.overtime.data.model.WorkDay
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class AddHrsExtrasViewModel : ViewModel() {

    private val _state = mutableStateOf(AddHrsExtrasState())
    val state = _state

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

    // Aquí puedes agregar la lógica para guardar el día de trabajo en Firebase
    fun addWorkDay(workDay: WorkDay) {
        viewModelScope.launch {
            // Aquí va la lógica para agregar el WorkDay en Firebase
            FirebaseFirestore.getInstance()
                .collection("workdays")
                .add(workDay)
                .addOnSuccessListener {
                    // Manejo de éxito
                }
                .addOnFailureListener {
                    // Manejo de error
                }
        }
    }
}
