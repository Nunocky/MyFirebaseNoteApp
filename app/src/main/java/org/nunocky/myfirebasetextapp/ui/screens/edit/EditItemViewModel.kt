package org.nunocky.myfirebasetextapp.ui.screens.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.nunocky.myfirebasetextapp.data.ItemDeleteUIState
import org.nunocky.myfirebasetextapp.data.ItemLoadUIState
import org.nunocky.myfirebasetextapp.data.ItemSaveUIState
import org.nunocky.myfirebasetextapp.domain.CloudStorageUseCase
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
        viewModelScope.launch(Dispatchers.IO) {
            _itemLoadUiState.value = ItemLoadUIState.Processing

            cloudStorageUseCase.getItem(
                itemId = itemId,
                onSuccess = { title, content ->
                    _itemLoadUiState.value = ItemLoadUIState.Success(Pair(title, content))
                },
                onError = { e ->
                    _itemLoadUiState.value = ItemLoadUIState.Error(e)
                }
            )
        }
    }

    fun updateItem(itemId: String, title: String, content: String) {
        viewModelScope.launch(Dispatchers.IO) {
            cloudStorageUseCase.updateItem(
                itemId = itemId,
                title = title,
                content = content,
                onSuccess = {
                    _itemSaveUiState.value = ItemSaveUIState.Success(itemId)
                },
                onError = { error ->
                    _itemSaveUiState.value = ItemSaveUIState.Error(error)
                }
            )
        }
    }

    fun deleteItem(itemId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            cloudStorageUseCase.deleteItem(
                itemId = itemId,
                onSuccess = {
                    _itemDeleteUiState.value = ItemDeleteUIState.Success(itemId)
                },
                onError = { error ->
                    _itemDeleteUiState.value = ItemDeleteUIState.Error(error)
                }
            )
        }
    }
}