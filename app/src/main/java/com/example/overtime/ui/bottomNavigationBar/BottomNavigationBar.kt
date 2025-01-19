package com.example.overtime.ui.bottomNavigationBar

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.overtime.navigation.currentRoute
import com.example.overtime.ui.theme.ButtonPrimary
import com.example.overtime.ui.theme.SecondaryColor

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
) {
    val menuItems = listOf(
        ItemsBottomNav.HomeScreen,
        ItemsBottomNav.ConfigScreen,
        ItemsBottomNav.AddHrsExtras,
    )
    BottomAppBar {
        NavigationBar(
            containerColor = Color.White,

        ) {
            menuItems.forEach { item ->
                val selected = currentRoute(navController) == item.route
                NavigationBarItem(
                    selected = selected,
                    onClick = { navController.navigate(item.route){
                        popUpTo(navController.graph.findStartDestination().id){saveState = true}
                        launchSingleTop = true
                    } },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    },
                    label = { Text(text = item.title) },
                    alwaysShowLabel = false,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SecondaryColor,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.Black,
                        indicatorColor = ButtonPrimary
                    )
                )
            }
        }
    }
}