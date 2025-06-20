package org.nunocky.myfirebasenoteapp.ui.screens.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.nunocky.myfirebasenoteapp.usecase.CloudStorageUseCase
import org.nunocky.myfirebasenoteapp.data.UIState
import javax.inject.Inject

@HiltViewModel
class EditItemViewModel @Inject constructor(
    private val cloudStorageUseCase: CloudStorageUseCase,
) : ViewModel() {

    private val _itemSaveUiState = MutableStateFlow<UIState>(UIState.Initial)
    val itemSaveUiState = _itemSaveUiState.asStateFlow()

    private val _itemLoadUiState = MutableStateFlow<UIState>(UIState.Initial)
    val itemLoadUiState = _itemLoadUiState.asStateFlow()

    private val _itemDeleteUiState = MutableStateFlow<UIState>(UIState.Initial)
    val itemDeleteUiState = _itemDeleteUiState.asStateFlow()

    fun loadItem(itemId: String) {
        viewModelScope.launch {
            _itemLoadUiState.update { UIState.Processing }

            withContext(Dispatchers.IO) {
                try {
                    val (title, content) = cloudStorageUseCase.getItem(itemId = itemId)
                    _itemLoadUiState.update { UIState.Success(Pair(title, content)) }
                } catch (e: Exception) {
                    _itemLoadUiState.update { UIState.Error(e) }
                }
            }
        }
    }

    fun updateItem(itemId: String, title: String, content: String) {
        viewModelScope.launch {
            _itemSaveUiState.update { UIState.Processing }
            withContext(Dispatchers.IO) {
                try {
                    cloudStorageUseCase.updateItem(
                        itemId = itemId,
                        title = title,
                        content = content,
                    )
                    _itemSaveUiState.update { UIState.Success(itemId) }
                } catch (e: Exception) {
                    _itemSaveUiState.update { UIState.Error(e) }
                }
            }
        }
    }

    fun deleteItem(itemId: String) {
        viewModelScope.launch {
            _itemDeleteUiState.update { UIState.Processing }

            withContext(Dispatchers.IO) {
                try {
                    cloudStorageUseCase.deleteItem(itemId)
                    _itemDeleteUiState.update { UIState.Success(Unit) }
                } catch (e: Exception) {
                    _itemDeleteUiState.update { UIState.Error(e) }
                }
            }
        }
    }
}