package com.example.overtime.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.overtime.ui.home.presenter.HomeScreen
import com.example.overtime.ui.login.presenter.LoginScreen
import com.example.overtime.ui.preLogin.presenter.PreLoginScreen
import com.example.overtime.ui.register.presenter.RegisterScreen

@Composable
fun NavGraph(
    navController: NavHostController

) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.PreLoginScreen.route
    ){

        composable(AppScreen.PreLoginScreen.route) {
            PreLoginScreen(
                navController = navController
            )
        }
        composable(AppScreen.LoginScreen.route) {
            LoginScreen(
                navController = navController
            )
        }
        composable(AppScreen.RegisterScreen.route) {
            RegisterScreen()
        }
        composable(AppScreen.HomeScreen.route) {
            HomeScreen()
        }

    }

}