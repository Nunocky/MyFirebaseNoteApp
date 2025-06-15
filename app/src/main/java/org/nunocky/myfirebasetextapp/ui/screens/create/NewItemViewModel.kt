package org.nunocky.myfirebasetextapp.ui.screens.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.nunocky.myfirebasetextapp.data.ItemSaveUIState
import org.nunocky.myfirebasetextapp.domain.CloudStorageUseCase
import javax.inject.Inject

@HiltViewModel
class NewItemViewModel @Inject constructor(
    private val cloudStorageUseCase: CloudStorageUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ItemSaveUIState>(ItemSaveUIState.Initial)
    val uiState = _uiState.asStateFlow()

    fun createNewItem(title: String, content: String) {
        viewModelScope.launch(Dispatchers.IO) {
            cloudStorageUseCase.createNewItem(
                title = title,
                content = content,
                onSuccess = { itemId ->
                    _uiState.value = ItemSaveUIState.Success(itemId)
                },
                onError = { error ->
                    _uiState.value = ItemSaveUIState.Error(error)
                }
            )
        }
    }
}