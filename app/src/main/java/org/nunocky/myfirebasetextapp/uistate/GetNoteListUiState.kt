package org.nunocky.myfirebasetextapp.uistate

sealed class GetNoteListUiState {
    object Initial : GetNoteListUiState()
    object Processing : GetNoteListUiState()
    data class Success(val itemList: List<Pair<String, String>>) : GetNoteListUiState()
    data class Error(val e: Throwable) : GetNoteListUiState()
}

