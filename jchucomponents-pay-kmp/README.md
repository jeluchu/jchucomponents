# JchuComponents Pay KMP

Shared RevenueCat purchase and subscription helpers for Compose Multiplatform
applications targeting Android, `iosArm64`, and `iosSimulatorArm64`.

The module uses RevenueCat's official `purchases-kmp-core` SDK. It supports
Android's Google Play Store and iOS's App Store through one API in `commonMain`.
The existing `jchucomponents-pay` artifact remains available for its Android
API and shared data utilities. RevenueCat's Kotlin Multiplatform SDK does not
currently publish a macOS target, so this artifact does not include macOS.

## Installation

Add the dependency to your shared source set:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("io.github.jeluchu:jchucomponents-pay-kmp:3.0.0-alpha23")
        }
    }
}
```

RevenueCat SDK 3.6.0 is added transitively.

## Initialization and purchases

Initialize the SDK once during app startup. Pass the public API key for the
active store (Google Play on Android or App Store on iOS), the debug flag, and
the entitlement identifier configured in RevenueCat:

```kotlin
import com.jeluchu.pay.revenuecat.kmp.Payment
import com.jeluchu.pay.revenuecat.kmp.initPayment

initPayment(
    apiKey = revenueCatApiKey,
    isDebug = isDebug,
    subscriptionName = "premium"
)

val billingInfo = Payment.loadBillingInfo()
val monthlyResult = Payment.purchaseResult(Payment.ProductsType.MONTHLY)
```

`Payment.billingInfo` and `Payment.billingError` expose `StateFlow` values for
observing changes from shared code. `Payment.getProducts()` refreshes that
state; `Payment.purchase()` offers a callback API; `restorePurchases()` and
`isSubscriptionActive()` cover restore and entitlement checks. The
`upgradeDownGrade()` helper switches between the monthly and annual packages
configured in the current offering.

The module does not render a paywall UI. Applications choose their own Compose
UI and pass the selected `Package` to RevenueCat or use the provided monthly
and annual purchase helpers.
