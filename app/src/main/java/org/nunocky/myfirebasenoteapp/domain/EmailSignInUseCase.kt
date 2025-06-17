package org.nunocky.myfirebasenoteapp.domain

import org.nunocky.myfirebasenoteapp.data.SignInResult

interface EmailSignInUseCase {
    suspend fun signIn(email: String, password: String): SignInResult
}
