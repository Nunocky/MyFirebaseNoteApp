package org.nunocky.myfirebasetextapp.data


sealed class ItemDeleteUIState {
    object Initial : ItemDeleteUIState()
    object Processing : ItemDeleteUIState()
    object Success : ItemDeleteUIState()
    data class Error(val e: Throwable) : ItemDeleteUIState()
}

//sealed class ItemDeleteUIState : UIState<String>()