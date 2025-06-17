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
import org.nunocky.myfirebasenoteapp.uistate.ItemDeleteUIState
import org.nunocky.myfirebasenoteapp.uistate.ItemLoadUIState
import org.nunocky.myfirebasenoteapp.uistate.ItemSaveUIState
import org.nunocky.myfirebasenoteapp.domain.CloudStorageUseCase
import javax.inject.Inject

@HiltViewModel
class EditItemViewModel @Inject constructor(
    private val cloudStorageUseCase: CloudStorageUseCase,
) : ViewModel() {

    private val _itemSaveUiState = MutableStateFlow<ItemSaveUIState>(ItemSaveUIState.Initial)
    val itemSaveUiState = _itemSaveUiState.asStateFlow()

    private val _itemLoadUiState = MutableStateFlow<ItemLoadUIState>(ItemLoadUIState.Initial)
    val itemLoadUiState = _itemLoadUiState.asStateFlow()

    private val _itemDeleteUiState = MutableStateFlow<ItemDeleteUIState>(ItemDeleteUIState.Initial)
    val itemDeleteUiState = _itemDeleteUiState.asStateFlow()

    fun loadItem(itemId: String) {
        viewModelScope.launch {
            _itemLoadUiState.update { ItemLoadUIState.Processing }

            withContext(Dispatchers.IO) {
                try {
                    val (title, content) = cloudStorageUseCase.getItem(itemId = itemId)
                    _itemLoadUiState.update { ItemLoadUIState.Success(Pair(title, content)) }
                } catch (e: Exception) {
                    _itemLoadUiState.update { ItemLoadUIState.Error(e) }
                }
            }
        }
    }

    fun updateItem(itemId: String, title: String, content: String) {
        viewModelScope.launch {
            _itemSaveUiState.update { ItemSaveUIState.Processing }
            withContext(Dispatchers.IO) {
                try {
                    cloudStorageUseCase.updateItem(
                        itemId = itemId,
                        title = title,
                        content = content,
                    )
                    _itemSaveUiState.update { ItemSaveUIState.Success(itemId) }
                } catch (e: Exception) {
                    _itemSaveUiState.update { ItemSaveUIState.Error(e) }
                }
            }
        }
    }

    fun deleteItem(itemId: String) {
        viewModelScope.launch {
            _itemDeleteUiState.update { ItemDeleteUIState.Processing }

            withContext(Dispatchers.IO) {
                try {
                    cloudStorageUseCase.deleteItem(itemId)
                    _itemDeleteUiState.update { ItemDeleteUIState.Success }
                } catch (e: Exception) {
                    _itemDeleteUiState.update { ItemDeleteUIState.Error(e) }
                }
            }
        }
    }
}