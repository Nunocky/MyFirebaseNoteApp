package org.nunocky.myfirebasenoteapp.uistate

sealed class ItemDeleteUIState {
    object Initial : ItemDeleteUIState()
    object Processing : ItemDeleteUIState()
    object Success : ItemDeleteUIState()
    data class Error(val e: Throwable) : ItemDeleteUIState()
}
