package com.example.overtime.presentation.home.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.overtime.presentation.home.component.HomeContent
import com.example.overtime.presentation.home.component.HomeActionsRow
import com.example.overtime.presentation.home.component.HomeSummaryCard
import com.example.overtime.presentation.home.viewModel.HomeViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.width
import androidx.compose.ui.unit.dp
import com.example.overtime.ui.theme.ButtonPrimary

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val currentMonth =
        remember { LocalDate.now().month.getDisplayName(TextStyle.FULL, Locale("es", "ES")) }
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

        HomeActionsRow(
            onDeleteAll = { viewModel.deleteAllWorkDays() },
            onDownloadPdf = { /* TODO: Descargar PDF */ }
        )

        HomeContent(
            workDays = workDays,
            onDeleteWorkDay = { workDay -> viewModel.deleteWorkDay(workDay) }
        )
    }
}


