package org.nunocky.myfirebasetextapp.domain

import org.nunocky.myfirebasetextapp.data.SignInResult
import org.nunocky.myfirebasetextapp.data.User

interface EmailSignInUseCase {
    suspend fun signIn(email: String, password: String): SignInResult
}
