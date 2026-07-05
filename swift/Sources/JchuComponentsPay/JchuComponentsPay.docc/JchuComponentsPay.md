# ``JchuComponentsPay``

Integrate RevenueCat subscriptions with display-ready native Swift models.

## Overview

Configure the shared service once during application startup:

```swift
JchuPayment.shared.configure(
    apiKey: revenueCatAPIKey,
    isDebug: isDebug,
    subscriptionName: "premium"
)
```

Request the current offering before presenting products:

```swift
JchuPayment.shared.getProducts()
```

Observe `billingInfo` from SwiftUI and initiate a purchase using the selected
RevenueCat package type:

```swift
JchuPayment.shared.purchase(type: .annual) { active, productIdentifier in
    if active {
        dismiss()
    }
} onFailure: { error, userCancelled in
    guard !userCancelled else { return }
    present(error)
}
```

## Topics

### Service

- ``JchuPayment``

### Subscription state

- ``JchuSubscriptionInfo``
- ``JchuSubscriptionType``
- ``JchuSubscriptionState``
- ``JchuBillingInfo``
- ``JchuPaymentProduct``
