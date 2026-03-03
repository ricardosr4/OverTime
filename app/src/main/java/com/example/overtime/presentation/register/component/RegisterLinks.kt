package com.example.overtime.presentation.register.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.overtime.R
import com.example.overtime.presentation.login.components.ZetaTextLink

@Composable
fun RegisterLinks(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    ZetaTextLink(
        text = stringResource(R.string.register_have_account),
        linkColor = MaterialTheme.colorScheme.primary,
        textLink = stringResource(R.string.register_login_here),
        onClick = { navController.navigate("login_screen") },
        modifier = modifier
    )
}