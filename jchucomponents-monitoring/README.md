# JchuComponents Monitoring

Unified crash reporting and analytics for Android, iOS, macOS and JVM desktop. See the
[JchuConfig guide](../docs/jchu-config.md) for how it fits the rest of the library.

```kotlin
JchuConfig(this) {
    crashlytics { collectionEnabled = !BuildConfig.DEBUG }
    analytics { collectionEnabled = !BuildConfig.DEBUG }
}

JchuMonitoring.crash.recordException(error, context = "Checkout")
JchuMonitoring.analytics.logEvent("purchase_started", mapOf("plan" to "yearly"))
```

- Firebase is optional and owned by the app. On Android the library compiles against it (`compileOnly`); on Apple
  targets it reaches `FIRCrashlytics` / `FIRAnalytics` through the Objective-C runtime, so no Swift code is needed.
  Without the SDK every call is a no-op.
- Event names, parameter names and values are sanitized to Firebase's limits (`JchuAnalyticsRules`).
- A backend that throws never breaks the app.
- Custom backends: pass `backend = ...` to `crashlytics {}` or `analytics {}`.
