package org.nunocky.myfirebasetextapp.data

sealed class SignInResult<T> {
    class Success<T>(val user: T) : SignInResult<T>()
    class Failed<T>(val exception: Exception) : SignInResult<T>()
    object Cancelled : SignInResult<Nothing>()
}