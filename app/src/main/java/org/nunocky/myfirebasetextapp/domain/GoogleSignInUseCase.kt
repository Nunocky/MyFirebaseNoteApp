package org.nunocky.myfirebasetextapp.domain

import org.nunocky.myfirebasetextapp.data.SignInResult

interface GoogleSignInUseCase {
    suspend fun signIn(googleClientId: String): SignInResult
}