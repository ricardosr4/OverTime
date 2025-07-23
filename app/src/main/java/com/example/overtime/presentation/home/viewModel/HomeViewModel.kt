package com.example.overtime.presentation.home.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.overtime.data.model.WorkDay
import com.example.overtime.domain.useCase.workday.GetWorkDaysUseCase
import com.example.overtime.domain.useCase.workday.DeleteWorkDayUseCase
import com.example.overtime.domain.useCase.workday.DeleteAllWorkDaysUseCase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWorkDaysUseCase: GetWorkDaysUseCase,
    private val deleteWorkDayUseCase: DeleteWorkDayUseCase,
    private val deleteAllWorkDaysUseCase: DeleteAllWorkDaysUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _workDays = MutableStateFlow<List<WorkDay>>(emptyList())
    val workDays: StateFlow<List<WorkDay>> = _workDays

    private var userId: String? = firebaseAuth.currentUser?.uid

    init {
        observeWorkDays()
    }

    private fun observeWorkDays() {
        userId?.let { uid ->
            viewModelScope.launch {
                getWorkDaysUseCase(uid).collectLatest { workDaysList ->
                    _workDays.value = workDaysList
                }
            }
        }
    }

    fun deleteAllWorkDays() {
        userId?.let { uid ->
            viewModelScope.launch {
                val result = deleteAllWorkDaysUseCase(uid)
                if (result.isSuccess) {
                    _workDays.value = emptyList()
                } else {
                    Log.e("WorkDay", "Error al eliminar todos los WorkDays: ${result.exceptionOrNull()?.message}")
                }
            }
        }
    }

    fun deleteWorkDay(workDay: WorkDay) {
        val workDayId = workDay.id
        userId?.let { uid ->
            if (!workDayId.isNullOrEmpty()) {
                viewModelScope.launch {
                    val result = deleteWorkDayUseCase(uid, workDayId)
                    if (result.isFailure) {
                        Log.e("WorkDay", "Error al eliminar el día de trabajo: ${result.exceptionOrNull()?.message}")
                    }
                }
            } else {
                Log.e("WorkDay", "El ID del WorkDay es nulo o vacío, no se puede eliminar.")
            }
        }
    }
}
