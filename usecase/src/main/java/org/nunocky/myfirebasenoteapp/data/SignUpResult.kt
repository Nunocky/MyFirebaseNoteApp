package org.nunocky.myfirebasenoteapp.data

sealed class SignUpResult {
    class Success(val user: User) : SignUpResult()
    class Failed(val exception: Exception) : SignUpResult()
}