# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Development Commands

### Build Commands
```bash
# Build the project
./gradlew build

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Clean build
./gradlew clean
```

### Testing Commands
```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Run specific test
./gradlew test --tests "ExampleUnitTest"
```

### Development Commands
```bash
# Install debug APK to connected device
./gradlew installDebug

# Run lint checks
./gradlew lint

# Generate dependency report
./gradlew dependencies
```

## Project Architecture

### Technology Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material 3
- **Architecture**: MVVM with Clean Architecture principles
- **Dependency Injection**: Dagger Hilt
- **Backend**: Firebase (Authentication, Firestore)
- **Navigation**: Jetpack Navigation Compose with type-safe navigation using Kotlin serialization

### Project Structure
```
app/src/main/java/org/nunocky/myfirebasetextapp/
├── MainActivity.kt                 # Entry point with @AndroidEntryPoint
├── MyApplication.kt               # Application class with @HiltAndroidApp
├── AppRouting.kt                  # Navigation setup with type-safe routes
├── data/                          # Data layer
├── domain/                        # Domain layer (use cases, interfaces)
├── di/                           # Dependency injection modules
└── ui/
    ├── screens/                  # Screen implementations
    │   ├── login/               # Login screen with Google Sign-In
    │   ├── home/                # Main screen with notes list
    │   └── create/              # Note creation screen
    └── theme/                   # Material 3 theming
```

### Key Architectural Components

#### Navigation (AppRouting.kt)
- Uses type-safe navigation with Kotlin serialization
- Three main destinations: `Home`, `Login`, `NewItem`
- Hilt ViewModels are injected at composable level using `hiltViewModel()`

#### Authentication Flow
- Google Sign-In integration via `GoogleSignInUseCase`
- Firebase Authentication with automatic user registration to Firestore
- Login screen handles authentication state and redirects

#### Data Architecture
- **Domain Layer**: Use cases define business logic interfaces
- **DI Layer**: Hilt modules provide dependency bindings
- **Firebase Integration**: Firestore for user data, Authentication for sign-in

#### UI Architecture
- **Compose-first**: All UI built with Jetpack Compose
- **Material 3**: Uses dynamic theming and Material 3 components
- **State Management**: ViewModels handle UI state, use cases handle business logic

### Firebase Configuration
- Requires `google-services.json` in `app/` directory
- Web client ID configured via `gradle.properties` as `WEB_CLIENT_ID`
- Firestore data structure: `users/{userId}/notes/{noteId}`

### Key Dependencies
- **Jetpack Compose**: UI framework with Material 3
- **Dagger Hilt**: Dependency injection
- **Firebase**: Authentication, Firestore, Analytics
- **Navigation Compose**: Type-safe navigation
- **Kotlin Serialization**: Route parameters and data serialization

### Development Notes
- The project is a Firebase learning application for note-taking
- Features Google authentication, note creation, and cloud storage
- Uses Japanese comments and documentation in some files
- Follows Material 3 design guidelines with FloatingActionButton for note creation