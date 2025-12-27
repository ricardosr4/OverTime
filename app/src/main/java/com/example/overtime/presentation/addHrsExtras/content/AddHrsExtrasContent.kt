package com.example.overtime.presentation.addHrsExtras.content

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.overtime.presentation.addHrsExtras.component.AddHrsExtrasButton
import com.example.overtime.presentation.addHrsExtras.component.AddHrsExtrasHeader
import com.example.overtime.presentation.addHrsExtras.component.DateSelectionCard
import com.example.overtime.presentation.addHrsExtras.component.HoursSelectionCard
import com.example.overtime.presentation.addHrsExtras.component.PercentageSelectionCard
import com.example.overtime.presentation.addHrsExtras.component.SummaryCard

@Composable
fun AddHrsExtrasContent(
    selectedDate: String,
    selectedPercentage: Int,
    selectedHours: Int,
    onDateButtonClick: () -> Unit,
    onPercentageSelected: (Int) -> Unit,
    onHoursSelected: (Int) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
//        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            AddHrsExtrasHeader()
        }
        
        item {
            DateSelectionCard(
                selectedDate = selectedDate,
                onDateButtonClick = onDateButtonClick
            )
        }
        
        item {
            PercentageSelectionCard(
                selectedPercentage = selectedPercentage,
                onPercentageSelected = onPercentageSelected
            )
        }
        
        item {
            HoursSelectionCard(
                selectedHours = selectedHours,
                onHoursSelected = onHoursSelected
            )
        }
        
        item {
            SummaryCard(
                selectedDate = selectedDate,
                selectedPercentage = selectedPercentage,
                selectedHours = selectedHours
            )
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        item {
            AddHrsExtrasButton(
                onAddClick = onAddClick
            )
        }
    }
} 