package org.nunocky.myfirebasetextapp.data


sealed class ItemLoadUIState {
    object Initial : ItemLoadUIState()
    object Processing : ItemLoadUIState()
    data class Success(val data: Pair<String, String>) : ItemLoadUIState()
    data class Error(val e: Throwable) : ItemLoadUIState()
}
