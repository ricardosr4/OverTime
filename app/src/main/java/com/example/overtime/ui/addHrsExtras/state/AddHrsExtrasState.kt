package com.example.overtime.ui.addHrsExtras.state

data class AddHrsExtrasState(
    val selectedDate: String = "Selecciona una fecha",
    val selectedPercentage: Int = 50,
    val selectedHours: Int = 1,
    val showErrorDialog: Boolean = false,
    val showDatePicker: Boolean = false
)
