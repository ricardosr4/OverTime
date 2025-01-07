package com.example.overtime.navigation

sealed class AppScreen(val route: String) {
    data object SplashScreen: AppScreen("splash_screen")
    data object PreLoginScreen: AppScreen("pre_login_screen")
    data object LoginScreen: AppScreen("login_screen")
    data object RegisterScreen: AppScreen("register_screen")
    data object HomeScreen: AppScreen("home_screen")
    data object ConfigScreen: AppScreen("config_screen")
    data object AddHrsExtras: AppScreen("add_hrs_extras")
}
