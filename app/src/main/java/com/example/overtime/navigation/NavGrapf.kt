package com.example.overtime.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.overtime.ui.addHrsExtras.AddHrsExtrasScreen
import com.example.overtime.ui.configuration.ConfigScreen
import com.example.overtime.ui.home.presenter.HomeScreen
import com.example.overtime.ui.login.presenter.LoginScreen
import com.example.overtime.ui.preLogin.presenter.PreLoginScreen
import com.example.overtime.ui.register.presenter.RegisterScreen
import com.example.overtime.ui.splasScreen.SplashScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navController: NavHostController

) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.SplashScreen.route
    ) {

        composable(AppScreen.SplashScreen.route) {
            SplashScreen(
                navController = navController
            )
        }
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
            RegisterScreen(
                navController = navController
            )

        }
        composable(AppScreen.HomeScreen.route) {
            HomeScreen(
                navController = navController
            )
        }
        composable(AppScreen.ConfigScreen.route) {
            ConfigScreen()
        }
        composable(AppScreen.AddHrsExtrasScreen.route) {
            AddHrsExtrasScreen()
        }


    }

}