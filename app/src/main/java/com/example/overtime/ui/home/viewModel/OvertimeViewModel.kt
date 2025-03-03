package com.example.overtime.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.overtime.data.model.WorkDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OvertimeViewModel : ViewModel() {

    private val _workDays = MutableStateFlow<List<WorkDay>>(emptyList())
    val workDays: StateFlow<List<WorkDay>> = _workDays

    fun addWorkDay(workDay: WorkDay) {
        _workDays.value = _workDays.value + workDay
    }
    fun deleteWorkDay(workDay: WorkDay) {
        _workDays.value = _workDays.value - workDay
    }
}
