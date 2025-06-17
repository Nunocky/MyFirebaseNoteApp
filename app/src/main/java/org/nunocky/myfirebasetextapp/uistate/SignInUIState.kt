package org.nunocky.myfirebasetextapp.uistate

import org.nunocky.myfirebasetextapp.data.User

sealed class SignInUIState {
    data object Initial : SignInUIState()
    data object Processing : SignInUIState()
    class Success(val user: User) : SignInUIState()
    data object Cancelled : SignInUIState()
    class Failed(val e: Exception) : SignInUIState()
}
