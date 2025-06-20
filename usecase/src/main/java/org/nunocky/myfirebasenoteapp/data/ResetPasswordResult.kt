package org.nunocky.myfirebasenoteapp.data

sealed class ResetPasswordResult {
    class Success() : ResetPasswordResult()
    class Failed(val exception: Exception) : ResetPasswordResult()
}