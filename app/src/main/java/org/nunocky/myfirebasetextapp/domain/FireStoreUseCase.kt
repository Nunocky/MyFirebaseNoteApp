package org.nunocky.myfirebasetextapp.domain

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireStoreUseCase @Inject constructor() : CloudStorageUseCase {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override suspend fun registerUser(user: FirebaseUser) {
        val userId = user.uid
        val displayName = user.displayName ?: "名無しさん" // 表示名を取得、またはデフォルト値
        val email = user.email ?: "メールアドレスなし" // メールアドレスを取得、またはデフォルト値

        // 保存したいユーザーデータのMapを作成
        val userData = hashMapOf(
            "displayName" to displayName,
            "email" to email,
            "createdAt" to Timestamp.Companion.now() // ユーザー作成日時なども追加できます
        )

        // usersコレクションの、ユーザーのUIDをIDとしたドキュメントにデータを書き込む
        db.collection("users").document(userId)
            .set(userData) // set()を使うと、ドキュメントがなければ作成、あれば上書き
            .await()
    }

    /**
     * ノートの idと titleのリストを返す
     */
    override suspend fun getItemList(): List<Pair<String, String>> {
        // 現在サインインしているユーザーを取得
        val user = auth.currentUser

        if (user == null) {
            throw Exception("ユーザーがサインインしていません。")
        }

        val userId = user.uid

        // usersコレクション -> {userId} ドキュメント -> notes サブコレクションを取得
        val querySnapshot = db.collection("users").document(userId).collection("notes")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .await()

        return querySnapshot.documents.map { document ->
            // ドキュメントからデータを取得して、必要な形式に変換
            val title = document.getString("title") ?: "タイトルなし"
            Pair(document.id, title)
        }
    }

    override suspend fun createNewItem(
        title: String,
        content: String,
    ): String {
        val user = auth.currentUser

        if (user == null) {
            throw Exception("ユーザーがサインインしていません。")
        }

        val userId = user.uid

        // 保存したいメモのデータを作成
        val noteData = hashMapOf(
            "title" to title,
            "content" to content,
            "createdAt" to Timestamp.now(),
            "updatedAt" to Timestamp.now()
        )

        // usersコレクション -> {userId} ドキュメント -> notes サブコレクションに新しいドキュメントを追加
        val reference = db.collection("users").document(userId).collection("notes")
            .add(noteData) // add()を使うと、IDを自動生成してドキュメントを追加
            .await()

        return reference.id
    }

    override suspend fun getItem(
        itemId: String,
    ): Pair<String, String> {
        val user = auth.currentUser

        if (user == null) {
            // ユーザーがサインインしていない場合はエラーを返す
            throw Exception("ユーザーがサインインしていません。")
        }

        val userId = user.uid

        val snapshot = db.collection("users").document(userId).collection("notes")
            .document(itemId) // itemIdを指定してドキュメントを取得
            .get() // get()を使うと、ドキュメントのデータを取得
            .await()

        val title = snapshot.getString("title") ?: "タイトルなし"
        val content = snapshot.getString("content") ?: "内容なし"
        return Pair(title, content)
    }

    override suspend fun updateItem(
        itemId: String,
        title: String,
        content: String,
    ) {
        val user = auth.currentUser

        if (user == null) {
            throw Exception("ユーザーがサインインしていません。")
        }

        val userId = user.uid

        // 更新したいメモのデータを作成
        val noteData = mapOf(
            "title" to title,
            "content" to content,
            "updatedAt" to Timestamp.now() // 更新日時も更新
        )

        // usersコレクション -> {userId} ドキュメント -> notes サブコレクションの itemId ドキュメントを更新
        db.collection("users").document(userId).collection("notes")
            .document(itemId) // itemIdを指定してドキュメントを取得
            .update(noteData) // set()を使うと、ドキュメントがなければ作成、あれば上書き
            .await()
    }

    override suspend fun deleteItem(itemId: String) {
        // 現在サインインしているユーザーを取得
        val user = auth.currentUser

        if (user == null) {
            throw Exception("ユーザーがサインインしていません。")
        }

        val userId = user.uid

        // usersコレクション -> {userId} ドキュメント -> notes サブコレクションの itemId ドキュメントを削除
        db.collection("users").document(userId).collection("notes")
            .document(itemId) // itemIdを指定してドキュメントを取得
            .delete() // delete()を使うと、ドキュメントを削除
            .await()
    }
}