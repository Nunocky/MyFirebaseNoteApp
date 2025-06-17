package org.nunocky.myfirebasetextapp.domain

import org.nunocky.myfirebasetextapp.data.SignInResult

interface EmailSignInUseCase {
    suspend fun signIn(email: String, password: String): SignInResult
}
