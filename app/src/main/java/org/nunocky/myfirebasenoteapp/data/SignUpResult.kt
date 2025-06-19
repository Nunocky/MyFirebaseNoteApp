package org.nunocky.myfirebasenoteapp.data

sealed class SignUpResult {
    class Success() : SignUpResult()
    class Failed(val exception: Exception) : SignUpResult()
}