# Log

## 1, Firebaseの設定

- Authentication
    - Google認証を有効化

## 2, Androidアプリ作成

- Android Studioで新規プロジェクトを作成
- 必要なライブラリを登録

## 3. Firebaseに Androidアプリを登録

- Firebaseプロジェクトに Androidアプリを追加
- `google-services.json`をダウンロードして、`app/`ディレクトリに配置

## 4. Firestoreで管理するデータ構造

以下のようなデータ構造を想定する。Firebase Consoleで設定することは特にない。

```
Firestore
└── users (コレクション)
    └── {userId} (ドキュメント)  <- Firebase AuthenticationのUID
        ├── (ユーザー情報フィールド例: displayName, emailなど)
        └── notes (サブコレクション)
            └── {noteId} (ドキュメント) <- 自動生成ID
                ├── title: "メモのタイトル"
                ├── content: "メモの内容"
                ├── createdAt: Timestamp
                └── updatedAt: Timestamp
```

### コード例: ユーザー情報の保存

```kt
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// Firestore インスタンスを取得
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
        "createdAt" to com.google.firebase.Timestamp.now() // ユーザー作成日時なども追加できます
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

```

### コード例: メモのサブコレクションへの保存

```kt

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// Firestore インスタンスを取得
val db = FirebaseFirestore.getInstance()

// 現在サインインしているユーザーを取得
val user = FirebaseAuth.getInstance().currentUser

user?.let {
    val userId = it.uid // ユーザーのUIDを取得

    // 保存したいメモのデータを作成
    val noteData = hashMapOf(
        "title" to "新しいメモのタイトル",
        "content" to "ここにメモの内容を書きます。",
        "createdAt" to Timestamp.now(),
        "updatedAt" to Timestamp.now()
    )

    // usersコレクション -> {userId} ドキュメント -> notes サブコレクションに新しいドキュメントを追加
    db.collection("users").document(userId).collection("notes")
        .add(noteData) // add()を使うと、IDを自動生成してドキュメントを追加
        .addOnSuccessListener { documentReference ->
            // 保存成功！ 生成されたドキュメントIDは documentReference.id で取得できます
            println("メモを Firestore に保存しました。ドキュメントID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            // 保存失敗...
            println("メモの保存に失敗しました: $e")
        }
}
```


### firestore.rules

```json
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // usersコレクションの{userId}ドキュメント内のnotesサブコレクションにマッチ
    match /users/{userId}/notes/{noteId} {
      // ログイン中のユーザーIDとドキュメントのパスにあるuserIdが一致する場合のみ
      // CRUD（作成、読み取り、更新、削除）を許可する
      allow read, write: if request.auth.uid == userId;
    }
  }
}
```

## ケース1：ログインユーザーの全メモを取得するコード

### メモのデータクラスを作成

```kt
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Note(
    val id: String = "", // ドキュメントIDを格納するため
    val title: String = "",
    val content: String = "",
    @ServerTimestamp
    val createdAt: Date? = null,
    @ServerTimestamp
    val updatedAt: Date? = null
)
```

### メモを取得する関数（ViewModelやRepository層に記述）

```kt
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class MemoRepository {

    private val db = Firebase.firestore
    private val auth = Firebase.auth

    suspend fun getAllNotes(): Result<List<Note>> {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            return Result.failure(Exception("User not logged in"))
        }

        return try {
            val userId = currentUser.uid
            val snapshot = db.collection("users")
                .document(userId)
                .collection("notes")
                .get()
                .await() // Coroutinesで完了を待つ

            val notes = snapshot.documents.mapNotNull { document ->
                // ドキュメントのデータをデータクラスに変換し、IDもセットする
                document.toObject(Note::class.java)?.copy(id = document.id)
            }
            Result.success(notes)

        } catch (e: Exception) {
            Log.e("MemoRepository", "Error getting notes", e)
            Result.failure(e)
        }
    }
}
```

## ケース2：新規アカウント作成時にユーザー情報をFirestoreに保存するコード

### ユーザーのデータクラスを作成

```kt
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class User(
    val displayName: String? = null,
    val email: String? = null,
    @ServerTimestamp
    val createdAt: Date? = null
    // photoURLやその他のアプリ独自フィールドもここに追加
)
```

### アカウントを作成し、ユーザー情報を保存する関数

```kt
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = Firebase.auth
    private val db = Firebase.firestore

    suspend fun createUser(email: String, password: String, displayName: String): Result<Unit> {
        return try {
            // 1. Firebase Authenticationでアカウントを作成
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: return Result.failure(Exception("Failed to create user."))

            // 2. Firestoreに保存するユーザー情報オブジェクトを作成
            val newUser = User(
                displayName = displayName,
                email = firebaseUser.email
            )

            // 3. Firestoreのusersコレクションにドキュメントを作成
            db.collection("users")
                .document(firebaseUser.uid)
                .set(newUser)
                .await() // Coroutinesで完了を待つ

            // 成功したら displayName もAuthのプロフィールに設定しておくと良い（任意）
            // val profileUpdates = userProfileChangeRequest { this.displayName = displayName }
            // firebaseUser.updateProfile(profileUpdates).await()

            Result.success(Unit)

        } catch (e: Exception) {
            Log.e("AuthRepository", "Error creating user", e)
            Result.failure(e)
        }
    }
}
```

### 

```kt

```

### 

```kt

```

