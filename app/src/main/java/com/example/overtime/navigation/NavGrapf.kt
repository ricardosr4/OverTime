package com.example.overtime.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.overtime.ui.screen.addHrsExtras.screen.AddHrsExtrasScreen
import com.example.overtime.ui.screen.addHrsExtras.viewModel.AddHrsExtrasViewModel
import com.example.overtime.ui.screen.configuration.ConfigScreen
import com.example.overtime.ui.screen.home.screen.HomeScreen
import com.example.overtime.ui.screen.home.viewModel.HomeViewModel
import com.example.overtime.ui.screen.login.view.BlankScreen
import com.example.overtime.ui.screen.login.view.LoginScreen
import com.example.overtime.ui.screen.preLogin.view.PreLoginScreen
import com.example.overtime.ui.screen.register.screen.RegisterScreen
import com.example.overtime.ui.screen.splasScreen.SplashScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: HomeViewModel,
    addHrsExtrasViewModel: AddHrsExtrasViewModel

) {


//    val viewModel: HomeViewModel = viewModel()
//    val addHrsExtrasViewModel: AddHrsExtrasViewModel = viewModel()

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
            AddHrsExtrasScreen(navController = navController, viewModel = addHrsExtrasViewModel)
        }
    }
}
