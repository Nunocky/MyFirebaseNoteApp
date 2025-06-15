package org.nunocky.myfirebasetextapp.data

import com.google.firebase.auth.FirebaseUser

sealed class SignInUIState {
    data object Initial : SignInUIState()
    data object Processing : SignInUIState()
    class Success(val user: FirebaseUser) : SignInUIState()
    class Failed(e: Exception) : SignInUIState()
}
