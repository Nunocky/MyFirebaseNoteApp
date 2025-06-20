package org.nunocky.myfirebasenoteapp.ui.screens.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.nunocky.myfirebasenoteapp.data.SignInResult
import org.nunocky.myfirebasenoteapp.data.UIState
import org.nunocky.myfirebasenoteapp.data.User
import org.nunocky.myfirebasenoteapp.usecase.Authentication
import org.nunocky.myfirebasenoteapp.usecase.CloudStorageUseCase
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authentication: Authentication,
    private val cloudStorageUseCase: CloudStorageUseCase,
) : ViewModel() {

    private val _signInUIState = MutableStateFlow<UIState>(UIState.Initial)
    val signInUIState = _signInUIState.asStateFlow()

    fun signInWithGoogle(googleClientId: String) {
        viewModelScope.launch {
            _signInUIState.update { UIState.Processing }

            withContext(Dispatchers.IO) {
                val result = authentication.googleSignIn(googleClientId)
                when (result) {
                    is SignInResult.Success<*> -> {
                        _signInUIState.update { UIState.Success(result.user as User) }
                    }

                    is SignInResult.Failed -> {
                        _signInUIState.update { UIState.Error(result.exception) }
                    }

                    is SignInResult.Cancelled -> {}
                }
            }
        }
    }

    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _signInUIState.update { UIState.Processing }

            withContext(Dispatchers.IO) {
                authentication.emailSignIn(email, password).let { result ->
                    when (result) {
                        is SignInResult.Success<*> -> {
                            _signInUIState.update { UIState.Success(result.user as User) }
                        }

                        is SignInResult.Failed -> {
                            _signInUIState.update { UIState.Error(result.exception) }
                        }

                        is SignInResult.Cancelled -> {
                            // Handle cancellation if needed
                        }
                    }
                }
            }
        }
    }

    fun registerUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            cloudStorageUseCase.registerUser(user)
        }
    }
}