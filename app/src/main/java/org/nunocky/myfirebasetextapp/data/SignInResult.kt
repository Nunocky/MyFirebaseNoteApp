package org.nunocky.myfirebasetextapp.data

import com.google.firebase.auth.FirebaseUser

sealed class SignInResult {
    class Success(val user: FirebaseUser) : SignInResult()
    class Failed(val exception: Exception) : SignInResult()
    object Cancelled : SignInResult()
}