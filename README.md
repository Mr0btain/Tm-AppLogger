# Tm-AppLogger

The logger shared by TicketMan and Tuners Toolkit: a Kotlin Multiplatform library for Android, JVM, wasmJs and iOS.

- `AppLogger.e/w/i/m/d(tag, message, throwable?)` writes to the platform's own log (logcat, java.util.logging,
  the browser console, NSLog). `d` only writes when `AppLogger.debug` is on. Messages can be a `String` or anything
  implementing `LogMessage`, like an app's error enum.
- `LogBook` keeps the newest lines in memory (`since(after)` for following along live, `last` as a `StateFlow`), and
  can save them to daily rotating files with `LogBook.attach(LogFileStore(folder, "myapp", LogSettings()))`. It also
  exports a time range.
- `LogFormat` is the file format: `2026-11-06 14:03:22.123 ERROR [Tag] message`, with a stack trace's later lines
  indented.

Tags are hierarchical constants: `private const val TAG = "Thing"`, then `AppLogger.e("$TAG:Load", "...")`.

## Using it

```kotlin
// libs.versions.toml: tm-applogger = { module = "org.mr0btain:tm-applogger", version = "0.1.0" }
commonMain.dependencies { implementation(libs.tm.applogger) }
```

The package is `org.mr0btain.applogger`. Apps build this from source rather than a Maven release. Their `settings.gradle.kts` includes it with
`includeBuild`, from the first of:

1. `<sharedLibsDir>/Tm-AppLogger`, with `sharedLibsDir` set in `~/.gradle/gradle.properties` (where you keep checkouts)
2. `../Tm-AppLogger`, checked out next to the app
3. `.deps/Tm-AppLogger`, cloned from the public mirror (`https://github.com/Mr0btain/Tm-AppLogger.git`) at the tag the app
   pins, on its first build

With 1 or 2, a change here shows up in the app straight away. A release is a `v*` tag matching
`tmapplogger.version`; an app moves to it by changing the tag in its `settings.gradle.kts` (and the version in its
`libs.versions.toml`). Building from source also means iOS works anywhere there's a Mac, with nothing to publish.

## Building

```bash
./gradlew jvmTest                                  # tests
./gradlew compileKotlinWasmJs compileAndroidMain   # the other targets compile
```

