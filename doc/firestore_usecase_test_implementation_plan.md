# FireStoreUseCaseのテスト実装方針

## 概要

このドキュメントは、`FireStoreUseCase`クラスの単体テスト実装方針をまとめたものです。既存のプロジェクトのテスト戦略と一貫性を保ちながら、Firebase特有の課題にも対応したテスト実装を目指します。

## 1. 分析結果サマリー

### プロジェクト構造
- **テスト対象**: `app/src/main/java/org/nunocky/myfirebasetextapp/domain/FireStoreUseCase.kt`
- **インターフェース**: `CloudStorageUseCase`
- **テスト配置**: `app/src/test/java/org/nunocky/myfirebasetextapp/domain/FireStoreUseCaseTest.kt`

### 既存テスト戦略
- **フレームワーク**: JUnit 4
- **モック**: Mockito + Mockito-Kotlin
- **非同期処理**: Kotlinx Coroutines Test
- **Flow テスト**: Turbine (必要に応じて)

## 2. テスト実装方針

### 2.1 基本戦略

**アプローチ**
- Firebase依存関係をMockして単体テスト化
- 既存のViewModelテストと同じライブラリ構成を使用
- 非同期処理は`runTest` + `TestDispatcher`で制御

**テスト範囲**
- 正常系: 認証済みユーザーでの各操作
- 例外系: 未認証状態、Firebase例外
- エッジケース: null値、空データの処理

### 2.2 テスト対象メソッド

| メソッド | 機能 | テストポイント |
|---------|------|-------------|
| `registerUser()` | ユーザー登録 | FirebaseUser情報の正しい保存 |
| `getItemList()` | ノート一覧取得 | 日付降順ソート、データ変換 |
| `createNewItem()` | ノート作成 | タイムスタンプ設定、ID返却 |
| `getItem()` | ノート取得 | データ存在確認、デフォルト値 |
| `updateItem()` | ノート更新 | updatedAt更新、部分更新 |
| `deleteItem()` | ノート削除 | 削除確認 |

### 2.3 モック戦略

**必要なMockオブジェクト**
```kotlin
@Mock private lateinit var firebaseFirestore: FirebaseFirestore
@Mock private lateinit var firebaseAuth: FirebaseAuth
@Mock private lateinit var mockUser: FirebaseUser
@Mock private lateinit var mockCollection: CollectionReference
@Mock private lateinit var mockDocument: DocumentReference
@Mock private lateinit var mockDocumentSnapshot: DocumentSnapshot
@Mock private lateinit var mockQuerySnapshot: QuerySnapshot
@Mock private lateinit var mockQuery: Query
```

**Firebase Task<>のモック**
```kotlin
// 成功時のTaskモック
val successTask = Tasks.forResult(mockDocumentReference)
whenever(mockCollection.add(any())).thenReturn(successTask)

// 例外時のTaskモック  
val failureTask = Tasks.forException<DocumentReference>(RuntimeException("Test exception"))
whenever(mockCollection.add(any())).thenReturn(failureTask)
```

## 3. 実装課題と対策

### 3.1 主要課題

**課題1: Firebase SDKの静的メソッド依存**
- 現状: `FirebaseFirestore.getInstance()`, `FirebaseAuth.getInstance()`
- 問題: 静的メソッドのためMockが困難
- 対策: 
  - 依存性注入への変更を検討
  - PowerMockまたはMockKの使用
  - テスト用のファクトリークラス導入

**課題2: Firebase特有のオブジェクト型**
- `Task<T>`, `DocumentSnapshot`, `QuerySnapshot`等
- 対策: 適切なMock設定とGoogle Play Services Tasksライブラリの活用

**課題3: Coroutines + Firebase統合**
- `.await()`拡張関数の挙動再現
- 対策: `Tasks.forResult()`と`Tasks.forException()`でTask生成

### 3.2 推奨する実装アプローチ

**Option A: 依存性注入への変更**
```kotlin
class FireStoreUseCase @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : CloudStorageUseCase {
    // 実装
}
```

**Option B: ファクトリーパターン**
```kotlin
interface FirebaseFactory {
    fun getFirestore(): FirebaseFirestore
    fun getAuth(): FirebaseAuth
}

class FireStoreUseCase @Inject constructor(
    private val firebaseFactory: FirebaseFactory
) : CloudStorageUseCase {
    // 実装
}
```

## 4. テスト実装例

### 4.1 基本構造

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class FireStoreUseCaseTest {
    @Mock private lateinit var firebaseFirestore: FirebaseFirestore
    @Mock private lateinit var firebaseAuth: FirebaseAuth
    @Mock private lateinit var mockUser: FirebaseUser
    @Mock private lateinit var mockCollection: CollectionReference
    @Mock private lateinit var mockDocument: DocumentReference
    
    private lateinit var useCase: FireStoreUseCase
    private val testDispatcher = StandardTestDispatcher()
    
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        
        // 共通のMock設定
        setupCommonMocks()
        
        // UseCaseインスタンス生成（DI変更後）
        useCase = FireStoreUseCase(firebaseFirestore, firebaseAuth)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    private fun setupCommonMocks() {
        whenever(firebaseAuth.currentUser).thenReturn(mockUser)
        whenever(mockUser.uid).thenReturn(TEST_USER_ID)
        whenever(mockUser.displayName).thenReturn(TEST_DISPLAY_NAME)
        whenever(mockUser.email).thenReturn(TEST_EMAIL)
        
        whenever(firebaseFirestore.collection("users")).thenReturn(mockCollection)
        whenever(mockCollection.document(TEST_USER_ID)).thenReturn(mockDocument)
        whenever(mockDocument.collection("notes")).thenReturn(mockCollection)
    }
}
```

### 4.2 テストケース例

```kotlin
@Test
fun `getItemList - 正常系 - ノートリストを取得する`() = runTest {
    // Given
    val mockQuerySnapshot = mock<QuerySnapshot>()
    val mockDocumentSnapshot = mock<DocumentSnapshot>()
    
    whenever(mockDocumentSnapshot.id).thenReturn("note1")
    whenever(mockDocumentSnapshot.getString("title")).thenReturn("テストタイトル")
    whenever(mockQuerySnapshot.documents).thenReturn(listOf(mockDocumentSnapshot))
    
    val mockQuery = mock<Query>()
    whenever(mockCollection.orderBy("createdAt", Query.Direction.DESCENDING)).thenReturn(mockQuery)
    whenever(mockQuery.get()).thenReturn(Tasks.forResult(mockQuerySnapshot))
    
    // When
    val result = useCase.getItemList()
    
    // Then
    assertEquals(1, result.size)
    assertEquals("note1", result[0].first)
    assertEquals("テストタイトル", result[0].second)
}

@Test
fun `getItemList - 例外系 - 未認証ユーザーでException発生`() = runTest {
    // Given
    whenever(firebaseAuth.currentUser).thenReturn(null)
    
    // When & Then
    val exception = assertThrows<Exception> {
        runBlocking { useCase.getItemList() }
    }
    assertEquals("ユーザーがサインインしていません。", exception.message)
}

@Test
fun `createNewItem - 正常系 - 新しいノートを作成してIDを返す`() = runTest {
    // Given
    val mockDocumentReference = mock<DocumentReference>()
    whenever(mockDocumentReference.id).thenReturn("generated_id")
    whenever(mockCollection.add(any())).thenReturn(Tasks.forResult(mockDocumentReference))
    
    // When
    val result = useCase.createNewItem("テストタイトル", "テスト内容")
    
    // Then
    assertEquals("generated_id", result)
    
    // Firestoreへの保存データを検証
    verify(mockCollection).add(argThat { noteData ->
        val data = noteData as HashMap<String, Any>
        data["title"] == "テストタイトル" && 
        data["content"] == "テスト内容"
    })
}
```

## 5. 実装順序

### Phase 1: 基盤整備
1. テスト依存関係の確認・追加
2. 基本的なMock構造の実装
3. 一つのメソッドでの動作確認

### Phase 2: 正常系実装
1. `getItemList()`の正常系テスト
2. `createNewItem()`の正常系テスト
3. その他メソッドの正常系テスト

### Phase 3: 例外系実装
1. 未認証ユーザーのテスト
2. Firebase例外のテスト
3. ネットワークエラーのテスト

### Phase 4: エッジケース実装
1. null値・空データの処理テスト
2. 日本語文字列の処理テスト
3. パフォーマンステスト（必要に応じて）

## 6. 追加の考慮事項

### 6.1 テスト環境での注意点
- Firebase Emulatorの使用も検討（統合テスト時）
- CI/CD環境でのFirebase認証情報管理
- テストデータの管理とクリーンアップ

### 6.2 パフォーマンステスト
- 大量データでのgetItemList()性能
- 並行アクセス時の動作確認

### 6.3 セキュリティテスト
- 他ユーザーのデータアクセス防止
- 認証状態の適切な確認

## 7. 参考資料

- [既存のViewModelテスト実装](../app/src/test/java/org/nunocky/myfirebasetextapp/ui/screens/)
- [Kotlinx Coroutines Testing Guide](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Firebase Testing Documentation](https://firebase.google.com/docs/rules/unit-tests)

---

*このドキュメントは FireStoreUseCase の単体テスト実装を成功させるための包括的なガイドです。実装時は段階的にアプローチし、既存のテストパターンとの一貫性を保つことを重視してください。*