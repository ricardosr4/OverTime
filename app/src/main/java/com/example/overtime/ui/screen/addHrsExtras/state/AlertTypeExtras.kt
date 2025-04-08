package com.example.overtime.ui.screen.addHrsExtras.state

sealed class AlertTypeExtras {
    data object EmptyField : AlertTypeExtras()
    data class UnknownError(val message: String) : AlertTypeExtras()
}
