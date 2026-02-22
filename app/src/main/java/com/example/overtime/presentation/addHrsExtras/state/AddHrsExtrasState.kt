package com.example.overtime.presentation.addHrsExtras.state

data class AddHrsExtrasState(
    val selectedDate: String = "Selecciona una fecha",
    val selectedPercentage: Int = 0,
    val selectedHours: Int = 0,
    val showErrorDialog: Boolean = false,
    val showDatePicker: Boolean = false,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val saveError: String? = null
)
