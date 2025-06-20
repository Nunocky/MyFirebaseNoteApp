package org.nunocky.myfirebasenoteapp.domain

import org.nunocky.myfirebasenoteapp.data.ResetPasswordResult
import org.nunocky.myfirebasenoteapp.data.SignInResult
import org.nunocky.myfirebasenoteapp.data.SignUpResult

interface EmailSignInUseCase {
    suspend fun signIn(email: String, password: String): SignInResult
    suspend fun signUp(email: String, password: String): SignUpResult
    suspend fun resetPassword(email: String): ResetPasswordResult
}
