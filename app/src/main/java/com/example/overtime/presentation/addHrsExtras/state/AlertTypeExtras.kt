package com.example.overtime.presentation.addHrsExtras.state

sealed class AlertTypeExtras {
    data object EmptyField : AlertTypeExtras()
    data class UnknownError(val message: String) : AlertTypeExtras()
}
