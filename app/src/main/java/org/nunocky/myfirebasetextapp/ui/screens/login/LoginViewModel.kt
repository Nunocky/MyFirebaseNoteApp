package org.nunocky.myfirebasetextapp.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.nunocky.myfirebasetextapp.data.SignInUIState
import org.nunocky.myfirebasetextapp.domain.CloudStorageUseCase
import org.nunocky.myfirebasetextapp.domain.GoogleSignInUseCase
import javax.inject.Inject

/**
 * Google Sign In
 * very thanks to https://qiita.com/kisayama/items/5dc7618b76f6d86a6d55
 */

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val cloudStorageUseCase: CloudStorageUseCase,
) : ViewModel() {

    private val _signInUIState = MutableStateFlow<SignInUIState>(SignInUIState.Initial)
    val signInUIState = _signInUIState.asStateFlow()

    fun signInWithGoogle(googleClientId: String) {
        viewModelScope.launch {
            _signInUIState.update { SignInUIState.Processing }

            withContext(Dispatchers.IO) {
                val result = googleSignInUseCase.signIn(googleClientId)
                when (result) {
                    is GoogleSignInUseCase.SignInResult.Success -> {
                        _signInUIState.update { SignInUIState.Success(result.user) }
                    }

                    is GoogleSignInUseCase.SignInResult.Failed -> {
                        _signInUIState.update { SignInUIState.Failed(result.exception) }
                    }
                }
            }
        }
    }

    fun registerUser(user: FirebaseUser) {
        viewModelScope.launch(Dispatchers.IO) {
            cloudStorageUseCase.registerUser(user)
        }
    }
}