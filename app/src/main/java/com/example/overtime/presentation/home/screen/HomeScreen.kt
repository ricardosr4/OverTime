package com.example.overtime.presentation.home.screen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.overtime.presentation.home.component.HomeActionsRow
import com.example.overtime.presentation.home.component.HomeContent
import com.example.overtime.presentation.home.component.HomeSummaryCard
import com.example.overtime.presentation.home.viewModel.HomeViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import android.net.Uri
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val currentMonth = remember { LocalDate.now().month.getDisplayName(TextStyle.FULL, Locale("es", "ES")) }
    val workDays by viewModel.workDays.collectAsState()
    val pdfResult by viewModel.pdfResult.collectAsState()
    val context = LocalContext.current

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
            onDownloadPdf = { viewModel.downloadWorkDaysPdf() }
        )

        HomeContent(
            workDays = workDays,
            onDeleteWorkDay = { workDay -> viewModel.deleteWorkDay(workDay) }
        )

        LaunchedEffect(pdfResult) {
            pdfResult?.let { result ->
                if (result.isSuccess) {
                    val value = result.getOrNull()
                    when (value) {
                        is File -> {
                            Toast.makeText(context, "PDF guardado en Descargas: ${value.name}", Toast.LENGTH_LONG).show()
                        }
                        is Uri -> {
                            Toast.makeText(context, "PDF guardado correctamente", Toast.LENGTH_LONG).show()
                        }
                    }
                    viewModel.clearPdfResult()
                } else if (result.isFailure) {
                    Toast.makeText(context, "Error al generar PDF: ${result.exceptionOrNull()?.localizedMessage}", Toast.LENGTH_LONG).show()
                    viewModel.clearPdfResult()
                }
            }
        }
    }
}


