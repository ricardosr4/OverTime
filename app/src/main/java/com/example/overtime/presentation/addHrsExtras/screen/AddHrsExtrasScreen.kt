package com.example.overtime.presentation.addHrsExtras.screen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.overtime.R
import com.example.overtime.data.model.WorkDay
import com.example.overtime.presentation.addHrsExtras.component.ErrorDialog
import com.example.overtime.presentation.addHrsExtras.content.AddHrsExtrasContent
import com.example.overtime.presentation.addHrsExtras.viewModel.AddHrsExtrasViewModel
import com.example.overtime.presentation.navigation.AppScreen
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHrsExtrasScreen(
    navController: NavController,
    viewModel: AddHrsExtrasViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.resetState()
    }

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            Toast.makeText(
                context,
                context.getString(R.string.add_hrs_success),
                Toast.LENGTH_SHORT
            ).show()
            navController.navigate(AppScreen.HomeScreen.route) {
                popUpTo(AppScreen.AddHrsExtrasScreen.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(state.saveError) {
        state.saveError?.let { error ->
            val messageRes = when (error) {
                "ERROR_USER_NOT_AUTHENTICATED" -> R.string.add_hrs_error_user_not_authenticated
                "ERROR_SAVE_GENERIC" -> R.string.add_hrs_error_save_generic
                else -> null
            }
            val message = messageRes?.let { context.getString(it) } ?: error
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AddHrsExtrasContent(
            modifier = Modifier.fillMaxSize(),
            selectedDate = state.selectedDate,
            selectedPercentage = state.selectedPercentage,
            selectedHours = state.selectedHours,
            onDateButtonClick = { viewModel.onShowDatePicker(true) },
            onPercentageSelected = { viewModel.onPercentageSelected(it) },
            onHoursSelected = { viewModel.onHoursSelected(it) },
            onAddClick = {
                if (!state.isSaving && viewModel.validateFields()) {
                    val newWorkDay = WorkDay(
                        weekDay = state.selectedDate,
                        quantityOverHours = state.selectedHours,
                        percentageOverHours = state.selectedPercentage
                    )
                    viewModel.addWorkDay(newWorkDay)
                } else if (!state.isSaving) {
                    viewModel.onShowErrorDialog(true)
                }
            }
        )
    }

    ErrorDialog(
        showDialog = state.showErrorDialog,
        onDismiss = { viewModel.onShowErrorDialog(false) }
    )

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
