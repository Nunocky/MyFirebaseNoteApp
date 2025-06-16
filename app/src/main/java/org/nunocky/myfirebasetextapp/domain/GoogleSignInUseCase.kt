package org.nunocky.myfirebasetextapp.domain

import org.nunocky.myfirebasetextapp.data.SignInResult
import org.nunocky.myfirebasetextapp.data.User

interface GoogleSignInUseCase {
    suspend fun signIn(googleClientId: String): SignInResult<User>
}