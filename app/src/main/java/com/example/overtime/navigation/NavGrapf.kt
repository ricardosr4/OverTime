package com.example.overtime.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.overtime.ui.addHrsExtras.AddHrsExtrasScreen
import com.example.overtime.ui.configuration.ConfigScreen
import com.example.overtime.ui.home.presenter.HomeScreen
import com.example.overtime.ui.login.presenter.BlankScreen
import com.example.overtime.ui.login.presenter.LoginScreen
import com.example.overtime.ui.preLogin.presenter.PreLoginScreen
import com.example.overtime.ui.register.presenter.RegisterScreen
import com.example.overtime.ui.splasScreen.SplashScreen
import com.example.overtime.ui.viewmodel.OvertimeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navController: NavHostController) {
    val viewModel: OvertimeViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = AppScreen.BlankScreen.route
    ) {
        composable(AppScreen.BlankScreen.route) {
            BlankScreen(navController = navController)
        }

        composable(AppScreen.SplashScreen.route) {
            SplashScreen(navController = navController)
        }
        composable(AppScreen.PreLoginScreen.route) {
            PreLoginScreen(navController = navController)
        }
        composable(AppScreen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(AppScreen.RegisterScreen.route) {
            RegisterScreen(navController = navController)
        }
        composable(AppScreen.HomeScreen.route) {
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable(AppScreen.ConfigScreen.route) {
            ConfigScreen(navController = navController)
        }
        composable(AppScreen.AddHrsExtrasScreen.route) {
            AddHrsExtrasScreen(navController = navController, viewModel = viewModel)
        }
    }
}
