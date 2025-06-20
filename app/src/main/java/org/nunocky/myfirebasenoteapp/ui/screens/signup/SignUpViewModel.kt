package org.nunocky.myfirebasenoteapp.ui.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.nunocky.myfirebasenoteapp.data.SignUpResult
import org.nunocky.myfirebasenoteapp.data.UIState
import org.nunocky.myfirebasenoteapp.usecase.Authentication
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    val authentication: Authentication,
) : ViewModel() {
    private val _signupUIState = MutableStateFlow<UIState>(UIState.Initial)
    val signupUIState = _signupUIState.asStateFlow()

    fun createAccount(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _signupUIState.update { UIState.Processing }
            try {
                val result = authentication.emailSignUp(email, password)
                when (result) {
                    is SignUpResult.Success -> {
                        _signupUIState.update { UIState.Success(result.user) }
                    }

                    is SignUpResult.Failed -> {
                        _signupUIState.update { UIState.Error(result.exception) }
                    }
                }
            } catch (e: Exception) {
                _signupUIState.update { UIState.Error(e) }
            }
        }
    }
}