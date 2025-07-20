package com.example.overtime.presentation.addHrsExtras.screen

import android.R.attr.padding
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.overtime.data.model.WorkDay
import com.example.overtime.presentation.navigation.AppScreen
import com.example.overtime.presentation.addHrsExtras.component.AddHrsExtrasContent
import com.example.overtime.presentation.addHrsExtras.component.ErrorDialog
import com.example.overtime.presentation.addHrsExtras.viewModel.AddHrsExtrasViewModel
import com.example.overtime.ui.theme.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHrsExtrasScreen(
    navController: NavController,
    viewModel: AddHrsExtrasViewModel = viewModel()
) {
    val state = viewModel.state.value

    // Resetear el estado cuando se entra a la pantalla
    LaunchedEffect(Unit) {
        viewModel.resetState()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) { innerPadding ->
        AddHrsExtrasContent(
            modifier = Modifier.padding(innerPadding),
            selectedDate = state.selectedDate,
            selectedPercentage = state.selectedPercentage,
            selectedHours = state.selectedHours,
            onDateButtonClick = { viewModel.onShowDatePicker(true) },
            onPercentageSelected = { viewModel.onPercentageSelected(it) },
            onHoursSelected = { viewModel.onHoursSelected(it) },
            onAddClick = {
                if (viewModel.validateFields()) {
                    val userId = Firebase.auth.currentUser?.uid ?: ""
                    val newWorkDay = WorkDay(
                        weekDay = state.selectedDate,
                        quantityOverHours = state.selectedHours,
                        percentageOverHours = state.selectedPercentage
                    )
                    viewModel.addWorkDay(newWorkDay)
                    navController.navigate(AppScreen.HomeScreen.route) {
                        popUpTo(AppScreen.AddHrsExtrasScreen.route) { inclusive = true }
                    }
                } else {
                    viewModel.onShowErrorDialog(true)
                }
            }
        )
    }
    
    // Diálogo de error
    ErrorDialog(
        showDialog = state.showErrorDialog,
        onDismiss = { viewModel.onShowErrorDialog(false) }
    )
    
    // DatePicker
    if (state.showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { viewModel.onShowDatePicker(false) },
            confirmButton = {
                TextButton(onClick = { viewModel.onShowDatePicker(false) }) {
                    Text("Aceptar")
                }
            }
        ) {
            val today = LocalDate.now()
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = today.toEpochDay() * 24 * 60 * 60 * 1000
            )
            
            DatePicker(
                state = datePickerState,
                dateValidator = { timestamp ->
                    val selectedDate = Instant.ofEpochMilli(timestamp)
                        .atZone(ZoneId.of("UTC"))
                        .toLocalDate()
                    // Solo permitir fechas hasta hoy (inclusive)
                    !selectedDate.isAfter(today)
                }
            )

            LaunchedEffect(datePickerState.selectedDateMillis) {
                datePickerState.selectedDateMillis?.let { millis ->
                    val localDate = Instant.ofEpochMilli(millis)
                        .atZone(ZoneId.of("UTC"))
                        .toLocalDate()

                    val formatter =
                        DateTimeFormatter.ofPattern("EEEE dd/MM/yyyy", Locale.getDefault())

                    val formattedDate =
                        localDate.format(formatter).replaceFirstChar { it.uppercase() }

                    viewModel.onDateSelected(formattedDate)
                }
            }
        }
    }
}