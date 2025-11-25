package com.example.overtime.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.overtime.presentation.addHrsExtras.screen.AddHrsExtrasScreen
import com.example.overtime.presentation.configuration.screen.ConfigScreen
import com.example.overtime.presentation.home.screen.HomeScreen
import com.example.overtime.presentation.navigation.AppScreen


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
            ConfigScreen(navController = navController)
        }
        composable(AppScreen.AddHrsExtrasScreen.route) {
            AddHrsExtrasScreen(
                navController = navController
            )
        }
    }
}