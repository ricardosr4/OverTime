package com.example.overtime.ui.screen.addHrsExtras.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.overtime.data.model.WorkDay
import com.example.overtime.ui.screen.addHrsExtras.viewModel.AddHrsExtrasViewModel
import com.example.overtime.ui.theme.ButtonPrimary
import com.example.overtime.ui.theme.CardColor
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHrsExtrasScreen(
    navController: NavController,
    viewModel: AddHrsExtrasViewModel = viewModel()
) {
    val state = viewModel.state.value

    val percentageOptions = listOf(50, 75, 100, 130)
    val hoursOptions = (1..12).toList()

    var expandedPercentage by remember { mutableStateOf(false) }
    var expandedHours by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardColor),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Seleccionar Fecha", fontSize = 16.sp, color = Color.Black)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { viewModel.onShowDatePicker(true) },
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonPrimary),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color.Black),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = state.selectedDate, fontSize = 16.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(50.dp))
            Divider()
            Spacer(modifier = Modifier.height(50.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = CardColor),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color.Black),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Selecciona el porcentaje de horas extras",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = expandedPercentage,
                        onExpandedChange = { expandedPercentage = !expandedPercentage }
                    ) {
                        TextField(
                            readOnly = true,
                            value = "${state.selectedPercentage}%",
                            onValueChange = {},
                            label = { Text("Porcentaje", color = Color.Black) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .border(
                                    1.dp,
                                    Color.Black,
                                    RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                                ),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.White
                            ),
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                            textStyle = LocalTextStyle.current.copy(color = Color.Black)

                        )
                        ExposedDropdownMenu(
                            expanded = expandedPercentage,
                            onDismissRequest = { expandedPercentage = false },
                            modifier = Modifier
                                .width(100.dp)
                                .background(Color.White)

                        ) {
                            percentageOptions.forEach { percentage ->
                                DropdownMenuItem(
                                    text = { Text("$percentage%", color = Color.Black) },
                                    onClick = {
                                        viewModel.onPercentageSelected(percentage)
                                        expandedPercentage = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(50.dp))
            Divider()
            Spacer(modifier = Modifier.height(50.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = CardColor),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color.Black),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Selecciona las horas extras", fontSize = 16.sp, color = Color.Black)
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = expandedHours,
                        onExpandedChange = { expandedHours = !expandedHours },

                        ) {
                        TextField(
                            readOnly = true,
                            value = "${state.selectedHours} hrs",
                            onValueChange = {},
                            label = { Text("Horas Extras", color = Color.Black) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    1.dp,
                                    Color.Black,
                                    RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                                )
                                .menuAnchor(),

                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.White
                            ),
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                            textStyle = LocalTextStyle.current.copy(color = Color.Black)
                        )
                        ExposedDropdownMenu(
                            expanded = expandedHours,
                            onDismissRequest = { expandedHours = false },
                            modifier = Modifier
                                .width(100.dp)
                                .background(Color.White),
                        ) {
                            hoursOptions.forEach { hour ->
                                DropdownMenuItem(
                                    text = { Text("$hour hrs", color = Color.Black) },
                                    onClick = {
                                        viewModel.onHoursSelected(hour)
                                        expandedHours = false
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
        Button(
            onClick = {
                if (state.selectedDate == "Selecciona una fecha" || state.selectedHours == 0) {
                    viewModel.onShowErrorDialog(true)
                } else {

                    val userId = Firebase.auth.currentUser?.uid ?: ""
                    val newWorkDay = WorkDay(
                        weekDay = state.selectedDate,
                        quantityOverHours = state.selectedHours,
                        percentageOverHours = state.selectedPercentage
                    )
                    viewModel.addWorkDay(newWorkDay)
                    navController.popBackStack()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = ButtonPrimary),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(20.dp)
        ) {
            Text(text = "Agregar", fontSize = 20.sp)
        }
    }
    if (state.showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { viewModel.onShowDatePicker(false) },
            confirmButton = {
                TextButton(onClick = { viewModel.onShowDatePicker(false) }) {
                    Text("Aceptar")
                }
            }
        ) {
            val datePickerState = rememberDatePickerState()
            DatePicker(state = datePickerState)

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