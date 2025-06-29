package org.nunocky.myfirebasenoteapp.ui.screens.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.nunocky.myfirebasenoteapp.usecase.CloudStorageUseCase
import org.nunocky.myfirebasenoteapp.data.UIState
import javax.inject.Inject

@HiltViewModel
class NewItemViewModel @Inject constructor(
    private val cloudStorageUseCase: CloudStorageUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Initial)
    val uiState = _uiState.asStateFlow()

    fun createNewItem(title: String, content: String) {
        viewModelScope.launch {
            _uiState.update { UIState.Processing }

            try {
                val itemId = cloudStorageUseCase.createNewItem(
                    title = title,
                    content = content,
                )
                _uiState.update { UIState.Success(itemId) }
            } catch (e: Exception) {
                _uiState.update { UIState.Error(e) }
            }
        }
    }
}