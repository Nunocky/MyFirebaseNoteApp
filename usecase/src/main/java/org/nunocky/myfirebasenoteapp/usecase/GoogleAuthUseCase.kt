package org.nunocky.myfirebasenoteapp.usecase

import org.nunocky.myfirebasenoteapp.data.SignInResult

interface GoogleAuthUseCase {
    suspend fun signIn(googleClientId: String): SignInResult
}