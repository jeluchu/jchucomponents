package com.jeluchu.pay.revenuecat.kmp

import com.jeluchu.pay.revenuecat.kmp.models.JchuCustomer
import com.jeluchu.pay.revenuecat.kmp.models.JchuEntitlement
import com.jeluchu.pay.revenuecat.kmp.models.JchuPackage
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.models.CustomerInfo
import com.revenuecat.purchases.kmp.models.Offering
import com.revenuecat.purchases.kmp.models.Offerings
import com.revenuecat.purchases.kmp.models.Package
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/** Failure of a [JchuPurchases] operation: RevenueCat error, timeout or purchases not configured. */
class JchuPurchasesException(
    message: String,
    cause: Throwable? = null
) : IllegalStateException(message, cause)

/** Outcome of [JchuPurchases.purchase]. A cancelled purchase is not an error. */
sealed interface JchuPurchaseResult {
    data class Purchased(
        val customer: JchuCustomer
    ) : JchuPurchaseResult

    data object Cancelled : JchuPurchaseResult
}

/**
 * Suspending, timeout-guarded access to RevenueCat for apps, so they do not call the SDK callbacks
 * themselves: offerings and packages, purchase and restore, user identity and the customer state.
 *
 * RevenueCat is configured by `payments { }` in `JchuConfig`. Without a usable key ([isConfigured] is
 * false) every operation fails with [JchuPurchasesException] and the app keeps working without purchases.
 *
 * ```
 * val offering = JchuPurchases.offering()                  // the current one, or offering("id")
 * val package = JchuPurchases.findPackage("annual")
 * when (val result = JchuPurchases.purchase(package)) {
 *     is JchuPurchaseResult.Purchased -> result.customer.isActive("premium")
 *     JchuPurchaseResult.Cancelled -> Unit
 * }
 * ```
 */
object JchuPurchases {
    /** Default timeout of reads and identity changes. */
    const val DEFAULT_TIMEOUT_MILLIS: Long = 15_000L

    /** Default timeout of purchases and restores, which wait for the store sheet. */
    const val DEFAULT_PURCHASE_TIMEOUT_MILLIS: Long = 60_000L

    private val identityMutex = Mutex()
    private var identifiedUserId: String? = null
    private var hasIdentifiedUser = false

    /** Last customer state seen by any operation, or null before the first one. */
    val customer: StateFlow<JchuCustomer?>
        field = MutableStateFlow<JchuCustomer?>(null)

    /** True once `payments { }` configured RevenueCat. */
    val isConfigured: Boolean
        get() = Purchases.isConfigured

    /** Offerings of the project; [Offerings.current] is the one marked as current in RevenueCat. */
    suspend fun offerings(timeoutMillis: Long = DEFAULT_TIMEOUT_MILLIS): Offerings {
        ensureConfigured()
        return await(timeoutMillis, "load the offerings") { onSuccess, onError ->
            Purchases.sharedInstance.getOfferings(onError = { onError(it.message) }, onSuccess = onSuccess)
        }
    }

    /** The offering with [id], or the current one when [id] is null. Null when it does not exist. */
    suspend fun offering(
        id: String? = null,
        timeoutMillis: Long = DEFAULT_TIMEOUT_MILLIS
    ): Offering? {
        val offerings = offerings(timeoutMillis)
        return if (id == null) offerings.current else offerings.all[id]
    }

    /**
     * The package with [packageId] in the offering [offeringId] (or in the current one when null).
     * Throws [JchuPurchasesException] when it is not there.
     */
    suspend fun findPackage(
        packageId: String,
        offeringId: String? = null,
        timeoutMillis: Long = DEFAULT_TIMEOUT_MILLIS
    ): Package =
        offering(offeringId, timeoutMillis)
            ?.availablePackages
            ?.firstOrNull { it.identifier == packageId }
            ?: throw JchuPurchasesException("RevenueCat package not found: $packageId")

    /** The packages of the offering [offeringId] (or the current one) as plain data for a paywall. */
    suspend fun packages(
        offeringId: String? = null,
        timeoutMillis: Long = DEFAULT_TIMEOUT_MILLIS
    ): List<JchuPackage> =
        offering(offeringId, timeoutMillis)?.availablePackages.orEmpty().map { it.toJchuPackage() }

    /**
     * Finds the package [packageId] (see [findPackage]) and buys it. [replacingProductId] is the product the user
     * has now, to switch plans: Google Play replaces it, the App Store applies its own subscription rules.
     */
    suspend fun purchase(
        packageId: String,
        offeringId: String? = null,
        replacingProductId: String? = null,
        timeoutMillis: Long = DEFAULT_PURCHASE_TIMEOUT_MILLIS
    ): JchuPurchaseResult =
        purchase(findPackage(packageId, offeringId), replacingProductId, timeoutMillis)

    /** Buys [packageToBuy]. Returns [JchuPurchaseResult.Cancelled] when the user dismisses the store sheet. */
    suspend fun purchase(
        packageToBuy: Package,
        replacingProductId: String? = null,
        timeoutMillis: Long = DEFAULT_PURCHASE_TIMEOUT_MILLIS
    ): JchuPurchaseResult {
        ensureConfigured()
        return await<CustomerInfo?>(timeoutMillis, "complete the purchase") { onSuccess, onError ->
            Purchases.sharedInstance.purchase(
                packageToPurchase = packageToBuy,
                oldProductId = replacingProductId,
                onError = { error, userCancelled ->
                    if (userCancelled) onSuccess(null) else onError(error.message)
                },
                onSuccess = { _, customerInfo -> onSuccess(customerInfo) }
            )
        }?.let { JchuPurchaseResult.Purchased(publish(it)) } ?: JchuPurchaseResult.Cancelled
    }

    /** Restores the purchases of the store account and returns the resulting customer. */
    suspend fun restore(timeoutMillis: Long = DEFAULT_PURCHASE_TIMEOUT_MILLIS): JchuCustomer {
        ensureConfigured()
        val info: CustomerInfo = await(timeoutMillis, "restore the purchases") { onSuccess, onError ->
            Purchases.sharedInstance.restorePurchases(onError = { onError(it.message) }, onSuccess = onSuccess)
        }
        return publish(info)
    }

    /** Fetches the customer from RevenueCat (cached by the SDK) and updates [customer]. */
    suspend fun refreshCustomer(timeoutMillis: Long = DEFAULT_TIMEOUT_MILLIS): JchuCustomer {
        ensureConfigured()
        val info: CustomerInfo = await(timeoutMillis, "load the customer") { onSuccess, onError ->
            Purchases.sharedInstance.getCustomerInfo(onError = { onError(it.message) }, onSuccess = onSuccess)
        }
        return publish(info)
    }

    /**
     * Makes RevenueCat's app user match [userId]: logs in when it is a different user, logs out when it is
     * null or blank, and does nothing when already identified, so it is safe to call on every screen.
     */
    suspend fun identify(
        userId: String?,
        timeoutMillis: Long = DEFAULT_TIMEOUT_MILLIS
    ) {
        ensureConfigured()
        identityMutex.withLock {
            val normalized = normalizeUserId(userId)
            if (hasIdentifiedUser && normalized == identifiedUserId) return
            val purchases = Purchases.sharedInstance
            if (normalized == null) {
                if (!purchases.isAnonymous) {
                    val info: CustomerInfo = await(timeoutMillis, "log out") { onSuccess, onError ->
                        purchases.logOut(onError = { onError(it.message) }, onSuccess = onSuccess)
                    }
                    publish(info)
                }
            } else if (purchases.appUserID != normalized) {
                val info: CustomerInfo = await(timeoutMillis, "log in") { onSuccess, onError ->
                    purchases.logIn(
                        newAppUserID = normalized,
                        onError = { onError(it.message) },
                        onSuccess = { customerInfo, _ -> onSuccess(customerInfo) }
                    )
                }
                publish(info)
            }
            identifiedUserId = normalized
            hasIdentifiedUser = true
        }
    }

    /** Forgets the app user and returns to an anonymous one. Same as `identify(null)`. */
    suspend fun logOut(timeoutMillis: Long = DEFAULT_TIMEOUT_MILLIS) = identify(null, timeoutMillis)

    private fun publish(info: CustomerInfo): JchuCustomer =
        info.toJchuCustomer(Purchases.sharedInstance.appUserID).also { customer.value = it }

    private fun ensureConfigured() {
        if (!Purchases.isConfigured) {
            throw JchuPurchasesException(
                "RevenueCat is not configured. Add the platform API key to payments { } in JchuConfig."
            )
        }
    }

    /**
     * Runs a callback based SDK call as a suspend function with a timeout. [block] receives the success and
     * the error callbacks; a null success value is allowed (used for a cancelled purchase).
     */
    private suspend fun <T> await(
        timeoutMillis: Long,
        action: String,
        block: (onSuccess: (T?) -> Unit, onError: (String) -> Unit) -> Unit
    ): T {
        val value =
            try {
                withTimeout(timeoutMillis) {
                    suspendCancellableCoroutine<T?> { continuation ->
                        block(
                            { if (continuation.isActive) continuation.resume(it) },
                            { message ->
                                if (continuation.isActive) {
                                    continuation.resumeWithException(JchuPurchasesException(message))
                                }
                            }
                        )
                    }
                }
            } catch (timeout: TimeoutCancellationException) {
                throw JchuPurchasesException("RevenueCat took too long to $action.", timeout)
            }
        @Suppress("UNCHECKED_CAST")
        return value as T
    }
}

/** App user ids are compared trimmed; blank means "no user". */
internal fun normalizeUserId(userId: String?): String? = userId?.trim()?.takeIf { it.isNotEmpty() }

private fun Package.toJchuPackage(): JchuPackage =
    JchuPackage(
        identifier = identifier,
        productIdentifier = storeProduct.id,
        title = storeProduct.title,
        description = storeProduct.localizedDescription.orEmpty(),
        formattedPrice = storeProduct.price.formatted,
        priceMicros = storeProduct.price.amountMicros,
        type = packageType.name
    )

private fun CustomerInfo.toJchuCustomer(appUserId: String): JchuCustomer =
    JchuCustomer(
        appUserId = appUserId,
        entitlements =
            entitlements.all.mapValues { (id, entitlement) ->
                JchuEntitlement(
                    identifier = id,
                    isActive = entitlement.isActive,
                    productIdentifier = entitlement.productIdentifier,
                    expirationMillis = entitlement.expirationDateMillis,
                    willRenew = entitlement.willRenew
                )
            }
    )
