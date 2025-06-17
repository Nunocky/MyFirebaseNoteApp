# AppRouting テスト方針

## 概要

AppRouting.ktは、Jetpack Navigation ComposeとKotlin Serializationを使用したtype-safe navigationを実装している。本ドキュメントでは、AppRoutingのテスト戦略と実装指針を定める。

## 対象コンポーネント

- `AppRouting.kt`（app/src/main/java/org/nunocky/myfirebasenoteapp/AppRouting.kt）
- ルート定義：`Home`, `Login`, `NewItem`, `EditItem`
- ナビゲーション制御ロジック

## テスト戦略

### 1. Unit Tests

#### 1.1 ルート定義のテスト
- **テスト対象**: @Serializableルートオブジェクト
- **テスト項目**:
  - `Home`, `Login`, `NewItem`オブジェクトのシリアライゼーション
  - `EditItem`データクラスのパラメータ渡し
  - ルートの一意性確認

```kotlin
// テスト例
@Test
fun `EditItem route should serialize correctly with itemId`() {
    val itemId = "test-item-123"
    val route = EditItem(itemId = itemId)
    assertEquals(itemId, route.itemId)
}
```

#### 1.2 ナビゲーション引数のテスト
- **テスト対象**: `EditItem`のitemIdパラメータ
- **テスト項目**:
  - 正常なitemId受け渡し
  - 空文字列・null値の処理
  - 特殊文字を含むitemIdの処理

### 2. Integration Tests

#### 2.1 ナビゲーション動作のテスト
- **テスト対象**: NavHostControllerを使用したナビゲーション
- **テスト項目**:
  - 各ルート間の遷移動作
  - 初期画面（Home）の表示確認
  - バックスタック操作
  - popBackStack()の動作確認

#### 2.2 コールバック処理のテスト
- **テスト対象**: 各画面のナビゲーションコールバック
- **テスト項目**:
  - `onLoginNeeded`: Home → Login遷移
  - `onCreateNewItem`: Home → NewItem遷移
  - `onRequestEditItem`: Home → EditItem遷移
  - `onLoginSuccess`: Loginからの戻り処理
  - `onSaveSuccess`: NewItem/EditItemからの戻り処理
  - `onEditCancelled`: 編集キャンセル処理

#### 2.3 ViewModelインジェクションのテスト
- **テスト対象**: hiltViewModel()による依存注入
- **テスト項目**:
  - 各画面で適切なViewModelが注入されること
  - ViewModelのライフサイクル管理
  - テスト環境でのモックViewModel注入

### 3. UI Tests

#### 3.1 実際の画面遷移テスト
- **テスト対象**: エンドツーエンドのナビゲーション
- **テスト項目**:
  - 実際のボタンタップによるナビゲーション
  - アプリ終了処理（LoginのonLoginCancelled）
  - ディープリンクからの画面表示

#### 3.2 状態保持のテスト
- **テスト対象**: 画面遷移時の状態保持
- **テスト項目**:
  - バックスタック経由での画面復帰
  - プロセス終了・復帰時の状態保持
  - 画面回転時の状態保持

## テスト実装指針

### テストファイル構成
```
app/src/test/java/org/nunocky/myfirebasenoteapp/
├── AppRoutingUnitTest.kt          # Unit Tests
└── navigation/
    ├── AppRoutingIntegrationTest.kt # Integration Tests
    └── NavigationTestUtils.kt       # テストユーティリティ

app/src/androidTest/java/org/nunocky/myfirebasenoteapp/
└── navigation/
    └── AppRoutingUITest.kt         # UI Tests
```

### 使用するテストライブラリ
- **Unit Tests**: JUnit4, Mockito/MockK
- **Integration Tests**: Robolectric, Navigation Testing library
- **UI Tests**: Espresso, Compose Testing library

### テスト環境設定
- **Navigation Testing**: `NavigationTestRule`の使用
- **Hilt Testing**: `@HiltAndroidTest`, `HiltTestApplication`の使用
- **Mock Objects**: `FakeNavHostController`の使用

## テスト実装優先度

### 高優先度
1. ルート定義の正確性テスト
2. 基本的なナビゲーション動作テスト
3. EditItemのitemId受け渡しテスト

### 中優先度
1. ViewModelインジェクションテスト
2. バックスタック操作テスト
3. コールバック処理テスト

### 低優先度
1. ディープリンクテスト
2. 状態保持テスト
3. エラーハンドリングテスト

## 継続的テスト戦略

- CI/CDパイプラインでのUnit/Integration Testsの実行
- プルリクエスト時のUI Tests実行
- 新機能追加時のナビゲーションテスト更新
- リグレッションテストの定期実行

## 注意事項

- type-safe navigationの特性を活かしたテスト設計
- 実際のAndroid環境とテスト環境の差異に注意
- Composeテストの非同期処理に注意
- Hiltのテスト用設定の適切な管理