package com.example.overtime.presentation.components

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

import com.example.overtime.presentation.navigation.currentRoute

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
) {
    val menuItems = listOf(
        ItemsBottomNav.HomeScreen,
        ItemsBottomNav.AddHrsExtras,
        ItemsBottomNav.ConfigScreen,
    )
    BottomAppBar {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface
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
                            contentDescription = stringResource(id = item.labelResId)
                        )
                    },
                    label = { Text(text = stringResource(id = item.labelResId)) },
                    alwaysShowLabel = false,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectedTextColor = MaterialTheme.colorScheme.onSurface,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        }
    }
}