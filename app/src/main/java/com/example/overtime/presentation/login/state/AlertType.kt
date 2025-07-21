package com.example.overtime.presentation.login.state

sealed class AlertType{
    data object EmptyField: AlertType()
    data object InvalidCredentials: AlertType()
    data object ResetPasswordSuccess: AlertType()
    data object ResetPasswordEmptyField: AlertType()
    data object ResetPasswordInvalidEmail: AlertType()
    data class UnknownError(val message: String): AlertType()
}
