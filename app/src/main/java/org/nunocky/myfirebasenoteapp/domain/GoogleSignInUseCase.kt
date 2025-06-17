package org.nunocky.myfirebasenoteapp.domain

import org.nunocky.myfirebasenoteapp.data.SignInResult

interface GoogleSignInUseCase {
    suspend fun signIn(googleClientId: String): SignInResult
}