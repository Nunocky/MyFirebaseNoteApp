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

    /**
     * ノートの idと titleのリストを返す
     *
     * TODO 新しいもの順に並べる
     *
     */
    override fun getItemList(
        onSuccess: (List<Pair<String, String>>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        // FireStore インスタンスを取得
        val db = FirebaseFirestore.getInstance()

        // 現在サインインしているユーザーを取得
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            // ユーザーがサインインしていない場合はエラーを返す
            onError(Exception("ユーザーがサインインしていません。"))
            return
        }

        val userId = user.uid // ユーザーのUIDを取得
        // usersコレクション -> {userId} ドキュメント -> notes サブコレクションを取得
        db.collection("users").document(userId).collection("notes")
            .get() // get()を使うと、ドキュメントの一覧を取得
            .addOnSuccessListener { querySnapshot ->

                val itemList = querySnapshot.documents.map { document ->
                    // ドキュメントからデータを取得して、必要な形式に変換
                    val title = document.getString("title") ?: "タイトルなし"
                    Pair(document.id, title)
                }
                onSuccess(itemList)
            }
            .addOnFailureListener { e ->
                // 取得失敗...
                println("メモの取得に失敗しました: $e")
                onError(e)
            }
    }

    override fun createNewItem(
        title: String,
        content: String,
        onSuccess: (String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        // FireStore インスタンスを取得
        val db = FirebaseFirestore.getInstance()

        // 現在サインインしているユーザーを取得
        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            // ユーザーがサインインしていない場合はエラーを返す
            onError(Exception("ユーザーがサインインしていません。"))
            return
        }

        val userId = user.uid // ユーザーのUIDを取得

        // 保存したいメモのデータを作成
        val noteData = hashMapOf(
            "title" to title,
            "content" to content,
            "createdAt" to Timestamp.now(),
            "updatedAt" to Timestamp.now()
        )

        // usersコレクション -> {userId} ドキュメント -> notes サブコレクションに新しいドキュメントを追加
        db.collection("users").document(userId).collection("notes")
            .add(noteData) // add()を使うと、IDを自動生成してドキュメントを追加
            .addOnSuccessListener { documentReference ->
                println("メモを FireStore に保存しました。ドキュメントID: ${documentReference.id}")
                onSuccess(documentReference.id)
            }
            .addOnFailureListener { e ->
                // 保存失敗...
                println("メモの保存に失敗しました: $e")
                onError(e)
            }
    }

    override fun getItem(
        itemId: String,
        onSuccess: (String, String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        // itemIdを指定してデータを取得する
        val db = FirebaseFirestore.getInstance()

        // 現在サインインしているユーザーを取得
        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            // ユーザーがサインインしていない場合はエラーを返す
            onError(Exception("ユーザーがサインインしていません。"))
            return
        }

        val userId = user.uid // ユーザーのUIDを取得

        db.collection("users").document(userId).collection("notes")
            .document(itemId) // itemIdを指定してドキュメントを取得
            .get() // get()を使うと、ドキュメントのデータを取得
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // ドキュメントが存在する場合
                    val title = document.getString("title") ?: "タイトルなし"
                    val content = document.getString("content") ?: "内容なし"
                    onSuccess(title, content)
                } else {
                    // ドキュメントが存在しない場合
                    onError(Exception("指定されたメモは存在しません。"))
                }
            }
            .addOnFailureListener { e ->
                // 取得失敗...
                println("メモの取得に失敗しました: $e")
                onError(e)
            }
    }

    override fun updateItem(
        itemId: String,
        title: String,
        content: String,
        onSuccess: (String) -> Unit,
        onError: (Throwable) -> Unit
    ) {

        // itemIdを指定してデータを取得する
        val db = FirebaseFirestore.getInstance()

        // 現在サインインしているユーザーを取得
        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            // ユーザーがサインインしていない場合はエラーを返す
            onError(Exception("ユーザーがサインインしていません。"))
            return
        }

        val userId = user.uid // ユーザーのUIDを取得

        // 更新したいメモのデータを作成
        val noteData = hashMapOf(
            "title" to title,
            "content" to content,
            "updatedAt" to Timestamp.now() // 更新日時も更新
        )
        // usersコレクション -> {userId} ドキュメント -> notes サブコレクションの itemId ドキュメントを更新
        db.collection("users").document(userId).collection("notes")
            .document(itemId) // itemIdを指定してドキュメントを取得
            .set(noteData) // set()を使うと、ドキュメントがなければ作成、あれば上書き
            .addOnSuccessListener {
                onSuccess(itemId)
            }
            .addOnFailureListener { e ->
                // 更新失敗...
                onError(e)
            }
    }

    override fun deleteItem(
        itemId: String,
        onSuccess: (String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        // FireStore インスタンスを取得
        val db = FirebaseFirestore.getInstance()

        // 現在サインインしているユーザーを取得
        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            // ユーザーがサインインしていない場合はエラーを返す
            onError(Exception("ユーザーがサインインしていません。"))
            return
        }

        val userId = user.uid // ユーザーのUIDを取得

        // usersコレクション -> {userId} ドキュメント -> notes サブコレクションの itemId ドキュメントを削除
        db.collection("users").document(userId).collection("notes")
            .document(itemId) // itemIdを指定してドキュメントを取得
            .delete() // delete()を使うと、ドキュメントを削除
            .addOnSuccessListener {
//                println("メモを FireStore から削除しました。ドキュメントID: $itemId")
                onSuccess(itemId)
            }
            .addOnFailureListener { e ->
                // 削除失敗...
//                println("メモの削除に失敗しました: $e")
                onError(e)
            }
    }
}