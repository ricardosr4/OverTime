package com.example.overtime.presentation.addHrsExtras.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.overtime.R
import com.example.overtime.presentation.login.components.ZetaAlertDialog

@Composable
fun ErrorDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        ZetaAlertDialog(
            title = stringResource(R.string.add_hrs_error_required_title),
            message = stringResource(R.string.add_hrs_error_required_message),
            confirmText = stringResource(R.string.add_hrs_error_required_confirm),
            onConfirmClick = onDismiss
        )
    }
}
