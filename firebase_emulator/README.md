# Firebase Emulator Suite のセットアップ

Firebase Emulator Suite をセットアップするための手順を以下に示します。

## 1. Firebase CLI のインストール

Firebase CLI をインストールするには、以下のコマンドを実行します。

```bash
npm install -g firebase-tools

## macOS の場合は、以下のコマンドを実行します。
brew install firebase-cli
```

## 2. Firebase プロジェクトの初期化

Firebase プロジェクトを初期化するには、以下のコマンドを実行します。

```bash
firebase init
```

このコマンドを実行すると、プロジェクトの設定を行うための対話式プロンプトが表示されます。必要なサービス（Firestore、Authentication）を選択してください。

## 3. 設定ファイル

firestore.rules

```text
rules_version = '2';

service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if false;
    }
  }
}
```

## 3. エミュレーターの起動

エミュレーターを起動するには、以下のコマンドを実行します。

```bash
firebase emulators:start
```

これにより、選択したサービスのエミュレーターが起動します。

## 4. エミュレーターの設定

エミュレーターの設定は、`firebase.json` ファイルで行います。以下は、Firestore と Authentication
のエミュレーターを設定する例です。

```json
{
  "emulators": {
    "firestore": {
      "port": 8080
    },
    "auth": {
      "port": 9099
    }
  }
}
```

## 5. ブラウザでのアクセス

エミュレーターが起動したら、ブラウザで以下の URL にアクセスして、エミュレーターのダッシュボードを確認できます。

- Firestore: `http://localhost:8080
- Authentication: `http://localhost:9099`
