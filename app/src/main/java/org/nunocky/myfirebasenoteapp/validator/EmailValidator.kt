package org.nunocky.myfirebasenoteapp.validator

class EmailValidator {
    companion object {
        fun isValidEmail(email: String): Boolean {
            // 簡易的なメールアドレスのバリデーション
            return email.contains("@") && email.contains(".") && email.length >= 5
        }
    }
}