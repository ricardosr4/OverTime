package com.example.overtime.presentation.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.overtime.data.model.WorkDay

@Composable
fun HomeSummaryCard(
    workDays: List<WorkDay>,
    currentMonth: String
) {
    val total50 = workDays.filter { it.percentageOverHours == 50 }.sumOf { it.quantityOverHours }
    val total75 = workDays.filter { it.percentageOverHours == 75 }.sumOf { it.quantityOverHours }
    val total100 = workDays.filter { it.percentageOverHours == 100 }.sumOf { it.quantityOverHours }
    val total130 = workDays.filter { it.percentageOverHours == 130 }.sumOf { it.quantityOverHours }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Horas Extras - $currentMonth",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = "$total130 hrs al 130%", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
            Text(text = "$total100 hrs al 100%", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
            Text(text = "$total75 hrs al 75%", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
            Text(text = "$total50 hrs al 50%", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}
