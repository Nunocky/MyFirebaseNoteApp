package org.nunocky.myfirebasenoteapp.ui.screens.resetpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.nunocky.myfirebasenoteapp.data.ResetPasswordResult
import org.nunocky.myfirebasenoteapp.data.UIState
import org.nunocky.myfirebasenoteapp.usecase.Authentication
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    val authentication: Authentication,
) : ViewModel() {
    private val _resetUIState = MutableStateFlow<UIState>(UIState.Initial)
    val resetUIState = _resetUIState.asStateFlow()

    fun requestResetPassword(email: String) {
        _resetUIState.update { UIState.Processing }

        viewModelScope.launch(Dispatchers.IO) {

            val result = authentication.resetPassword(email)

            when (result) {
                is ResetPasswordResult.Success -> {
                    authentication.signOut()
                    _resetUIState.update { UIState.Success(Unit) }
                }

                is ResetPasswordResult.Failed -> {
                    authentication.signOut()
                    _resetUIState.update { UIState.Error(Exception("Reset password failed")) }
                }
            }
        }
    }
}