package org.nunocky.myfirebasenoteapp.data

sealed class UIState {
    data object Initial : UIState()
    data object Processing : UIState()
    class Success<T>(val data: T) : UIState()
    class Error(val e: Exception) : UIState()
    data object Cancelled : UIState()
}