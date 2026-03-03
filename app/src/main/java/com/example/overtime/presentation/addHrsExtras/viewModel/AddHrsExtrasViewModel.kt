package com.example.overtime.presentation.addHrsExtras.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.overtime.data.model.WorkDay
import com.example.overtime.domain.useCase.workday.AddWorkDayUseCase
import com.example.overtime.presentation.addHrsExtras.state.AddHrsExtrasState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AddHrsExtrasViewModel @Inject constructor(
    private val addWorkDayUseCase: AddWorkDayUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

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

    fun resetState() {
        _state.value = AddHrsExtrasState()
    }

    private fun isFutureDate(dateString: String): Boolean {
        return try {
            val formatter = DateTimeFormatter.ofPattern("EEEE dd/MM/yyyy", Locale.getDefault())
            val selectedDate = LocalDate.parse(dateString, formatter)
            val today = LocalDate.now()
            val isFuture = selectedDate.isAfter(today)
            isFuture
        } catch (e: Exception) {
            false
        }
    }

    fun validateFields(): Boolean {
        val currentState = _state.value
        if (currentState.selectedDate.isBlank()) {
            return false
        }
        if (isFutureDate(currentState.selectedDate)) {
            return false
        }
        if (currentState.selectedHours < 1 || currentState.selectedHours > 12) {
            return false
        }
        val validPercentages = listOf(50, 75, 100, 130)
        if (!validPercentages.contains(currentState.selectedPercentage)) {
            return false
        }
        return true
    }

    fun addWorkDay(workDay: WorkDay) {
        _state.value = _state.value.copy(isSaving = true, saveError = null)
        viewModelScope.launch {
            val userId = firebaseAuth.currentUser?.uid
            if (userId != null) {
                val result = addWorkDayUseCase(userId, workDay)
                if (result.isSuccess) {
                    _state.value = _state.value.copy(isSaving = false, saveSuccess = true)
                } else {
                    _state.value = _state.value.copy(
                        isSaving = false,
                        saveError = result.exceptionOrNull()?.localizedMessage ?: "ERROR_SAVE_GENERIC"
                    )
                }
            } else {
                _state.value = _state.value.copy(isSaving = false, saveError = "ERROR_USER_NOT_AUTHENTICATED")
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        resetState()
    }
}
