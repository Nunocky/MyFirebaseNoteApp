package org.nunocky.myfirebasetextapp.domain

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FireStoreUseCase @Inject constructor() : CloudStorageUseCase {
    override fun registerUser(user: FirebaseUser) {
        val db = FirebaseFirestore.getInstance()

        // 現在サインインしているユーザーを取得
        val user = FirebaseAuth.getInstance().currentUser

        user?.let {
            val userId = it.uid // ユーザーのUIDを取得
            val displayName = it.displayName ?: "名無しさん" // 表示名を取得、またはデフォルト値
            val email = it.email ?: "メールアドレスなし" // メールアドレスを取得、またはデフォルト値

            // 保存したいユーザーデータのMapを作成
            val userData = hashMapOf(
                "displayName" to displayName,
                "email" to email,
                "createdAt" to Timestamp.Companion.now() // ユーザー作成日時なども追加できます
            )

            // usersコレクションの、ユーザーのUIDをIDとしたドキュメントにデータを書き込む
            db.collection("users").document(userId)
                .set(userData) // set()を使うと、ドキュメントがなければ作成、あれば上書き
                .addOnSuccessListener {
                    // 保存成功！
                    println("ユーザー情報を Firestore に保存しました: $userId")
                }
                .addOnFailureListener { e ->
                    // 保存失敗...
                    println("ユーザー情報の保存に失敗しました: $e")
                }
        }
    }
}