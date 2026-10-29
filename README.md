# TaskFlow

TaskFlow is an Android productivity app for keeping notes, organizing tasks into groups, reading quotes, and translating text. The Android application ID is `com.neupanesushant.note`; the project folder is named `Note`.

## Features

- Create, edit, browse, and delete notes.
- Organize tasks into groups and track task completion and dates.
- Browse quotes and keep quote data cached locally.
- Translate text with Google ML Kit Translate.
- Store app data locally with Room.

## Tech stack

- Kotlin and Android Views with View Binding
- Room for local persistence
- AndroidX Lifecycle and Coroutines
- Koin dependency injection
- Retrofit and Gson for quote API access
- Google ML Kit Translate

## Requirements

- Android Studio with Android SDK 36 configured.
- JDK compatible with the included Gradle wrapper.
- Android 7.0 (API 24) or later to run the app.

## Build and run

Open this directory in Android Studio and sync Gradle, or run from the project root:

```bash
./gradlew :app:assembleDebug
```

To install the debug build on a connected device or emulator:

```bash
./gradlew :app:installDebug
```

The release build uses signing values supplied through the local Gradle environment. Configure your own release signing credentials locally; never commit signing passwords or keystore files.

## Data and privacy

Notes and task data are stored on the device in a Room database. The app also has network access for quote API requests and translation features. Review `privacy_policy` alongside the current app behavior before distributing the application.
