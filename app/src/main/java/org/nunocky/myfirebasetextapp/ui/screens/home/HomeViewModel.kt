package org.nunocky.myfirebasetextapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.nunocky.myfirebasetextapp.data.GetNoteListUiState
import org.nunocky.myfirebasetextapp.domain.CloudStorageUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cloudStorageUseCase: CloudStorageUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<GetNoteListUiState>(GetNoteListUiState.Initial)
    val uiState = _uiState.asStateFlow()

    fun getNoteList() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = GetNoteListUiState.Processing

            cloudStorageUseCase.getItemList(
                onSuccess = { itemList ->
                    _uiState.value = GetNoteListUiState.Success(itemList)
                },
                onError = { e ->
                    _uiState.value = GetNoteListUiState.Error(e)
                }
            )
        }
    }
}