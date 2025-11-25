package com.example.overtime.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.overtime.presentation.addHrsExtras.screen.AddHrsExtrasScreen
import com.example.overtime.presentation.addHrsExtras.viewModel.AddHrsExtrasViewModel
import com.example.overtime.presentation.configuration.screen.ConfigScreen
import com.example.overtime.presentation.home.screen.HomeScreen
import com.example.overtime.presentation.home.viewModel.HomeViewModel
import com.example.overtime.presentation.login.screen.BlankScreen
import com.example.overtime.presentation.login.screen.LoginScreen
import com.example.overtime.presentation.preLogin.ui.PreLoginScreen
import com.example.overtime.presentation.register.screen.RegisterScreen
import com.example.overtime.presentation.splasScreen.SplashScreen
import androidx.hilt.navigation.compose.hiltViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navController: NavHostController
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
            val showOnlyLoader = it.arguments?.getBoolean("showOnlyLoader") ?: false
            SplashScreen(navController = navController, showOnlyLoader = showOnlyLoader)
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
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(navController = navController, viewModel = homeViewModel)
        }
        composable(AppScreen.ConfigScreen.route) {
            ConfigScreen(navController = navController)
        }
        composable(AppScreen.AddHrsExtrasScreen.route) {
            val addHrsExtrasViewModel: AddHrsExtrasViewModel = hiltViewModel()
            AddHrsExtrasScreen(navController = navController, viewModel = addHrsExtrasViewModel)
        }
    }
}
