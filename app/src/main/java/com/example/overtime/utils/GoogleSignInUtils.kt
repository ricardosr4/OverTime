package com.example.overtime.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

private const val TAG = "GoogleSignIn"

fun getGoogleSignInClient(context: Context): GoogleSignInClient {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("782417725602-045du8t2rpeh6t9sv6otmpt063rg3va7.apps.googleusercontent.com")
        .requestEmail()
        .build()
    return GoogleSignIn.getClient(context, gso)
}

fun getGoogleSignInIntent(context: Context): Intent {
    return getGoogleSignInClient(context).signInIntent
}

/**
 * @return el GoogleSignInAccount si fue exitoso, o null si falló.
 * El código de error se puede obtener por separado con [getGoogleSignInErrorCode].
 */
fun getGoogleAccountFromIntent(data: Intent?): GoogleSignInAccount? {
    val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
    return try {
        task.getResult(ApiException::class.java)
    } catch (e: ApiException) {
        Log.e(TAG, "Google Sign-In falló con statusCode=${e.statusCode}, message=${e.message}", e)
        null
    } catch (e: Exception) {
        Log.e(TAG, "Error inesperado en Google Sign-In: ${e.message}", e)
        null
    }
}

/**
 * Extrae el código de error de Google Sign-In del Intent para mostrarlo al usuario.
 * Códigos comunes: 10=DEVELOPER_ERROR (SHA-1), 12500=SIGN_IN_FAILED, 12501=CANCELLED
 */
fun getGoogleSignInErrorCode(data: Intent?): Int? {
    return try {
        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
        task.getResult(ApiException::class.java)
        null
    } catch (e: ApiException) {
        e.statusCode
    } catch (_: Exception) {
        null
    }
}
