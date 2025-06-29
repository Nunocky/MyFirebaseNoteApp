package org.nunocky.myfirebasenoteapp.data

sealed class SignInResult {
    class Success<T>(val user: T) : SignInResult()
    class Failed(val exception: Exception) : SignInResult()
    object Cancelled : SignInResult()
}