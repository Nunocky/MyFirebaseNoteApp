package org.nunocky.myfirebasetextapp.ui.screens.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    fun updateItem(contentId: String, title: String, content: String) {
        viewModelScope.launch(Dispatchers.IO) {
//        cloudStorageUseCase.createNewItem(
//            title = title,
//            content = content,
//            onSuccess = { itemId ->
//                _uiState.value = ItemSaveUIState.Success(itemId)
//            },
//            onError = { error ->
//                _uiState.value = ItemSaveUIState.Error(error)
//            }
//        )
        }
    }
}