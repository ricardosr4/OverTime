package com.example.overtime.presentation.home.screen

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.overtime.R
import com.example.overtime.presentation.home.component.HomeActionsRow
import com.example.overtime.presentation.home.component.HomeContent
import com.example.overtime.presentation.home.component.HomeSummaryCard
import com.example.overtime.presentation.home.viewModel.HomeViewModel
import java.io.File
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val currentMonth = remember {
        LocalDate.now().month
            .getDisplayName(TextStyle.FULL, Locale("es", "ES"))
            .replaceFirstChar { ch ->
                if (ch.isLowerCase()) ch.titlecase(Locale("es", "ES")) else ch.toString()
            }
    }
    val workDays by viewModel.workDays.collectAsState()
    val pdfResult by viewModel.pdfResult.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        HomeSummaryCard(
            workDays = workDays,
            currentMonth = currentMonth
        )

        HomeActionsRow(
            hasWorkDays = workDays.isNotEmpty(),
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
                    when (val value = result.getOrNull()) {
                        is File -> {
                            Toast.makeText(
                                context,
                                context.getString(R.string.home_pdf_saved_downloads, value.name),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        is Uri -> {
                            Toast.makeText(
                                context,
                                context.getString(R.string.home_pdf_saved),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    viewModel.clearPdfResult()
                } else if (result.isFailure) {
                    Toast.makeText(
                        context,
                        context.getString(
                            R.string.home_pdf_error,
                            result.exceptionOrNull()?.localizedMessage ?: ""
                        ),
                        Toast.LENGTH_LONG
                    ).show()
                    viewModel.clearPdfResult()
                }
            }
        }
    }
}
