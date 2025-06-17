package org.nunocky.myfirebasenoteapp.uistate

import org.nunocky.myfirebasenoteapp.data.User

sealed class SignInUIState {
    data object Initial : SignInUIState()
    data object Processing : SignInUIState()
    class Success(val user: User) : SignInUIState()
    data object Cancelled : SignInUIState()
    class Failed(val e: Exception) : SignInUIState()
}
