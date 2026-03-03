package com.example.overtime.presentation.configuration.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.overtime.core.prefs.PreferencesManager
import com.example.overtime.core.prefs.ThemeMode
import com.example.overtime.core.pdf.MonthlyPdfScheduler
import com.example.overtime.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val themeModeFlow: StateFlow<ThemeMode> = preferencesManager.themeModeFlow

    val monthClosingDayFlow: StateFlow<Int> = preferencesManager.monthClosingDayFlow

    val notificationsEnabledFlow: StateFlow<Boolean> = preferencesManager.notificationsEnabledFlow

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _userInfo = MutableStateFlow(
        Pair(
            appContext.getString(R.string.config_user_default_name),
            appContext.getString(R.string.config_user_email_unavailable)
        )
    )
    val userInfo: StateFlow<Pair<String, String>> = _userInfo.asStateFlow()

    fun toggleTheme() {
        val current = preferencesManager.getThemeMode()
        val next = when (current) {
            ThemeMode.DARK -> ThemeMode.LIGHT
            ThemeMode.LIGHT -> ThemeMode.DARK
            ThemeMode.SYSTEM -> ThemeMode.DARK
        }
        preferencesManager.setThemeMode(next)
    }

    /**
     * @return true si es la primera vez que se configura, false si se cambió un valor existente.
     */
    fun setMonthClosingDay(day: Int): Boolean {
        val previous = preferencesManager.getMonthClosingDay()
        preferencesManager.setMonthClosingDay(day)
        if (day > 0) {
            MonthlyPdfScheduler.scheduleMonthlyPdfDownload(appContext, day)
        } else {
            MonthlyPdfScheduler.cancelMonthlyPdfDownload(appContext)
        }
        return previous == 0
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        preferencesManager.setNotificationsEnabled(enabled)
    }

    fun getCurrentUser(): Pair<String, String> {
        val auth = Firebase.auth
        val user = auth.currentUser

        if (user != null) {
            val userId = user.uid
            val email = user.email ?: appContext.getString(R.string.config_user_email_unavailable)

            Firebase.firestore.collection("Users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val userName = document.getString("userName")
                            ?: appContext.getString(R.string.config_user_default_name)
                        _userInfo.value = Pair(userName, email)
                    } else {
                        _userInfo.value = Pair(
                            appContext.getString(R.string.config_user_default_name),
                            email
                        )
                    }
                }
                .addOnFailureListener {
                    _userInfo.value = Pair(
                        appContext.getString(R.string.config_user_default_name),
                        email
                    )
                }

            return _userInfo.value
        } else {
            return Pair(
                appContext.getString(R.string.config_user_default_name),
                appContext.getString(R.string.config_user_email_unavailable)
            )
        }
    }

    fun signOut(navController: NavController, context: Context) {
        val auth = Firebase.auth
        try {
            viewModelScope.launch {
                _isLoading.value = true
                auth.signOut()
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("782417725602-045du8t2rpeh6t9sv6otmpt063rg3va7.apps.googleusercontent.com")
                    .requestEmail()
                    .build()
                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                googleSignInClient.signOut()
                delay(1000)
                _isLoading.value = false
                navController.navigate("login_screen")
            }
        } catch (e: Exception) {
            _isLoading.value = false
            Toast.makeText(
                navController.context,
                navController.context.getString(
                    R.string.config_logout_snackbar_error,
                    e.localizedMessage ?: ""
                ),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
