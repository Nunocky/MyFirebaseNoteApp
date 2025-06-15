package org.nunocky.myfirebasetextapp.data


sealed class ItemSaveUIState {
    object Initial : ItemSaveUIState()
    object Processing : ItemSaveUIState()
    data class Success(val itemId: String) : ItemSaveUIState()
    data class Error(val e: Throwable) : ItemSaveUIState()
}
