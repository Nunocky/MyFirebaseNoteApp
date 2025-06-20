package org.nunocky.myfirebasenoteapp.usecase

import org.nunocky.myfirebasenoteapp.data.ResetPasswordResult
import org.nunocky.myfirebasenoteapp.data.SignInResult
import org.nunocky.myfirebasenoteapp.data.SignUpResult
import org.nunocky.myfirebasenoteapp.data.User

interface Authentication {
    fun currentUser(): User?
    fun signOut()

    suspend fun googleSignIn(googleClientId: String): SignInResult

    suspend fun emailSignIn(email: String, password: String): SignInResult
    suspend fun emailSignUp(email: String, password: String): SignUpResult
    suspend fun resetPassword(email: String): ResetPasswordResult
}

