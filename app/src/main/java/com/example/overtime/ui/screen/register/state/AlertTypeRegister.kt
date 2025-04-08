package com.example.overtime.ui.screen.register.state

sealed class AlertTypeRegister {
    data object EmptyField : AlertTypeRegister()
    data object InvalidEmail : AlertTypeRegister()
    data object InvalidPassword : AlertTypeRegister()
    data class UnknownError(val message: String) : AlertTypeRegister()
}
