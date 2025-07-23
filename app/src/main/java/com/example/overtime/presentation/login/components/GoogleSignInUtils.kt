package com.example.overtime.presentation.login.components

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

fun getGoogleSignInClient(context: Context): GoogleSignInClient {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("782417725602-045du8t2rpeh6t9sv6otmpt063rg3va7.apps.googleusercontent.com") // Web client id de Firebase
        .requestEmail()
        .build()
    return GoogleSignIn.getClient(context, gso)
}

fun getGoogleSignInIntent(context: Context): Intent {
    return getGoogleSignInClient(context).signInIntent
}

fun getGoogleAccountFromIntent(data: Intent?): GoogleSignInAccount? {
    val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
    return try {
        task.getResult(ApiException::class.java)
    } catch (e: Exception) {
        null
    }
} 