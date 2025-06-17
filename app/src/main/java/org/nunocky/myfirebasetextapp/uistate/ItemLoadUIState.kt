package org.nunocky.myfirebasetextapp.uistate

sealed class ItemLoadUIState {
    object Initial : ItemLoadUIState()
    object Processing : ItemLoadUIState()
    data class Success(val data: Pair<String, String>) : ItemLoadUIState()
    data class Error(val e: Throwable) : ItemLoadUIState()
}