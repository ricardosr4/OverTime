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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomNavGraph(
    navController: NavHostController

) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.HomeScreen.route
    ) {

        composable(AppScreen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(AppScreen.ConfigScreen.route) {
            ConfigScreen()
        }
        composable(AppScreen.AddHrsExtrasScreen.route) {
            AddHrsExtrasScreen()
        }
    }
}