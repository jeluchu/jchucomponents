# JchuComponents Room

Small Kotlin Multiplatform infrastructure helpers for Room 3 (`androidx.room3`) on Android and Apple platforms.

## What it configures

`configureJchuRoom` configures a `RoomDatabase.Builder` with `BundledSQLiteDriver` and a background coroutine context, then returns the same builder. It uses `Dispatchers.IO` on Android and `Dispatchers.Default` on Apple because Kotlin/Native does not expose `Dispatchers.IO`. The caller retains the builder and can add callbacks, migrations, journal configuration, connection pools, or any other Room option before calling `build()`.

```kotlin
val database = Room.databaseBuilder<AppDatabase>(context, "app.db")
    .configureJchuRoom {
        addMigrations(sqlMigration(1, 2, "ALTER TABLE task ADD COLUMN archived INTEGER NOT NULL DEFAULT 0"))
    }
    .build()
```

`sqlMigration` creates a Room 3 `Migration` backed by `SQLiteConnection` and executes each supplied statement in order. Room runs migrations in its transaction; the helper does not add a nested transaction.

## Database paths and data preservation

This module intentionally does not choose database paths. Keep the exact database name/path already used by an app, particularly on iOS, to preserve existing data. Supply that name/path to the platform's `Room.databaseBuilder` before calling `configureJchuRoom`.

## Consumer setup

This runtime module does not apply KSP transitively. Each consuming module that declares `@Database`, entities, or DAOs must apply KSP and add `androidx.room3:room3-compiler` itself. It must also use Room 3 imports (`androidx.room3.*`), not Room 2 (`androidx.room.*`).

No destructive-migration fallback is enabled by this module.

## Supported targets

Room 3.0 currently publishes Android, `iosArm64`, and `iosSimulatorArm64` variants, which are the targets published by this module. Its current artifacts do not include `iosX64` or macOS variants.
