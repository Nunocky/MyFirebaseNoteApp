package org.nunocky.myfirebasenoteapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.nunocky.myfirebasenoteapp.usecase.Authentication
import org.nunocky.myfirebasenoteapp.usecase.CloudStorageUseCase
import org.nunocky.myfirebasenoteapp.data.UIState
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cloudStorageUseCase: CloudStorageUseCase,
    val authentication: Authentication,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UIState>(UIState.Initial)
    val uiState = _uiState.asStateFlow()

    fun getNoteList() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { UIState.Processing }

            try {
                val itemList = cloudStorageUseCase.getItemList()
                _uiState.update { UIState.Success(itemList) }
            } catch (e: Exception) {
                _uiState.update { UIState.Error(e) }
            }
        }
    }
}