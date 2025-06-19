package org.nunocky.myfirebasenoteapp.ui.screens.createaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.nunocky.myfirebasenoteapp.data.UIState
import org.nunocky.myfirebasenoteapp.domain.Authentication
import org.nunocky.myfirebasenoteapp.domain.EmailSignInUseCase
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    val authentication: Authentication,
    val emailSignInUseCase: EmailSignInUseCase
) : ViewModel() {
    private val _signupUIState = MutableStateFlow<UIState>(UIState.Initial)
    val signupUIState = _signupUIState.asStateFlow()

    fun createAccount(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _signupUIState.update { UIState.Processing }
            try {
                emailSignInUseCase.createAccount(email, password)
                _signupUIState.update { UIState.Success(Unit) }
            } catch (e: Exception) {
                _signupUIState.update { UIState.Error(e) }
            }
        }
    }
}