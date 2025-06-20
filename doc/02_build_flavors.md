# Build Flavors

Firebaseプロジェクトを「開発環境」と「本番環境」に分け、開発時を本番運用時で異なるサーバにアクセスできるようにする。

## Firebaseプロジェクトの設定

[01_firebase_setup.md](01_firebase_setup.md) を参考に、Firebaseコンソールでプロジェクトを2つ作成する。

- 本番環境 **MyFirebaseNoteApp**
- 開発環境 **MyFirebaseNoteAppDev**

構成はどちらも同じにしておく。

MyFirebaseNoteAppDevに対して Androidアプリを登録する際は、本番環境の applicationId に `.dev`
を追加して登録する (後述の applicationIdSuffixによる)。

### app/build.gradle.kts

Androidアプリの Build Flavorsを設定する。
"prod" と "dev" の2つのフレーバーを作成する。 prodが本番環境、devが開発環境となる。

Cloud FireStoreの WEB_CLIENT_IDも設定する。

```kts
flavorDimensions += "env"
productFlavors {
    create("prod") {
        dimension = "env"
        applicationIdSuffix = ""

        buildConfigField(
            "String",
            "WEB_CLIENT_ID",
            project.properties["WEB_CLIENT_ID"] as String
        )
    }
    create("dev") {
        dimension = "env"
        applicationIdSuffix = ".dev"

        buildConfigField(
            "String",
            "WEB_CLIENT_ID",
            project.properties["WEB_CLIENT_ID_DEV"] as String
        )
    }
}
```

### app/gradle.properties

`app/gradle.properties` に以下のように設定しておく。

```text
WEB_CLIENT_ID="2992...."
WEB_CLIENT_ID_DEV="2142...."
```