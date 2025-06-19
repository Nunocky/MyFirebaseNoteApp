package org.nunocky.myfirebasenoteapp.validator

class PasswordValidator {
    companion object {
        fun isValidPassword(password: String): Boolean {
            // 8文字以上、アルファベットの大文字と数字をそれぞれ一つ以上使うこと
            return password.length >= 8 && password.any { it.isUpperCase() } && password.any { it.isDigit() }
        }
    }
}