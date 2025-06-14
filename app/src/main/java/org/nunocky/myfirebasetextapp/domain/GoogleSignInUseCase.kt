package org.nunocky.myfirebasetextapp.domain

import com.google.firebase.auth.FirebaseUser

interface GoogleSignInUseCase {
    sealed class SignInResult {
        class Success(val user: FirebaseUser) : SignInResult()
        class Failed(val exception: Exception) : SignInResult()
    }

    suspend fun signIn(googleClientId: String): SignInResult
}