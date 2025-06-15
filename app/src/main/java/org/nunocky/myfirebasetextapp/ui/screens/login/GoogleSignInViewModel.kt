package org.nunocky.myfirebasetextapp.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.nunocky.myfirebasetextapp.data.SignInUIState
import org.nunocky.myfirebasetextapp.domain.GoogleSignInUseCase
import javax.inject.Inject

/**
 * Google Sign In
 * very thanks to https://qiita.com/kisayama/items/5dc7618b76f6d86a6d55
 */

@HiltViewModel
class GoogleSignInViewModel @Inject constructor(
    private val googleSignInUseCase: GoogleSignInUseCase
) : ViewModel() {

    private val _signInUIState = MutableStateFlow<SignInUIState>(SignInUIState.Initial)
    val signInUIState = _signInUIState.asStateFlow()

    fun signIn(googleClientId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _signInUIState.value = SignInUIState.Processing

            when (val result = googleSignInUseCase.signIn(googleClientId)) {
                is GoogleSignInUseCase.SignInResult.Success -> {
                    _signInUIState.value = SignInUIState.Success(result.user)
                }

                is GoogleSignInUseCase.SignInResult.Failed -> {
                    _signInUIState.value = SignInUIState.Failed(result.exception)
                }
            }
        }
    }
}