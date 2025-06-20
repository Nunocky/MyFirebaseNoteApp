package org.nunocky.myfirebasenoteapp.usecase

import org.nunocky.myfirebasenoteapp.data.SignInResult

interface GoogleSignInUseCase {
    suspend fun signIn(googleClientId: String): SignInResult
}