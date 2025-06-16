テスト計画書

このプロジェクトに追加すべきテストを以下のカテゴリで提案します。

## 1. Unit Tests (ローカル実行)

### ViewModelテスト

- [x] `HomeViewModel` - ノート一覧の取得、削除処理のテスト
- [x] `NewItemViewModel` - ノート作成のテスト
- [x] `EditItemViewModel` - ノート編集のテスト
- `LoginViewModel` - ログイン状態管理のテスト

### Use Caseテスト

- `FireStoreUseCase` - Firestore操作のmockテスト
- `GoogleSignInUseCase` - サインイン処理のmockテスト
- `CloudStorageUseCase` - ストレージ操作のmockテスト

### データクラステスト

- `GetNoteListUiState`、`ItemSaveUIState`などの状態変更テスト

## 2. Integration Tests (Android実行)

### Navigation テスト

- `AppRouting` - 画面遷移のテスト
- Deep linkの動作確認

### Compose UI テスト

- `HomeRoute` - リスト表示、アイテム操作のUIテスト
- `NewItemRoute` - フォーム入力と保存のUIテスト
- `LoginRoute` - サインインボタンの動作テスト

### Repository テスト

- Firebase実装の統合テスト（テスト用DB使用）

## 3. 推奨実装順序

1. **ViewModelテスト** - ビジネスロジックが集中している
2. **Use Caseテスト** - 外部依存をmockして単体テスト
3. **Compose UIテスト** - ユーザー操作の確認
4. **統合テスト** - エンドツーエンドの動作確認

## 4. テスト環境構築

### 必要な依存関係

- JUnit 4/5
- Mockito/MockK
- Compose Test
- Hilt Test
- Coroutines Test
- Truth (Google Testing Library)

### テスト実行コマンド

```bash
# Unit テスト実行
./gradlew test

# Integration テスト実行  
./gradlew connectedAndroidTest

# 特定のテスト実行
./gradlew test --tests "ViewModelTest"
```