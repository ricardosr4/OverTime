package com.example.overtime.ui.screen.home.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.overtime.ui.screen.home.component.HomeContent
import com.example.overtime.ui.screen.home.component.HomeDeleteAllButton
import com.example.overtime.ui.screen.home.component.HomeSummaryCard
import com.example.overtime.ui.screen.home.viewModel.HomeViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val currentMonth = remember { LocalDate.now().month.getDisplayName(TextStyle.FULL, Locale("es", "ES")) }
    val workDays by viewModel.workDays.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        HomeSummaryCard(
            workDays = workDays,
            currentMonth = currentMonth
        )
        
        HomeDeleteAllButton(
            onDeleteAll = { viewModel.deleteAllWorkDays() }
        )
        
        HomeContent(
            workDays = workDays,
            onDeleteWorkDay = { workDay -> viewModel.deleteWorkDay(workDay) }
        )
    }
}


