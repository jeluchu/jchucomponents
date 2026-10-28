package com.jeluchu.pay.revenuecat.kmp

import com.jeluchu.pay.revenuecat.kmp.models.BillingInfo
import com.jeluchu.pay.revenuecat.kmp.models.Product
import com.jeluchu.pay.revenuecat.kmp.models.SubscriptionInfo
import com.jeluchu.pay.revenuecat.kmp.models.SubscriptionState
import com.jeluchu.pay.revenuecat.kmp.models.SubscriptionsType
import com.revenuecat.purchases.kmp.LogLevel
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.configure
import com.revenuecat.purchases.kmp.models.Package
import com.revenuecat.purchases.kmp.models.PackageType
import com.revenuecat.purchases.kmp.models.PurchasesError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.pow
import kotlin.math.roundToLong
import kotlin.time.Instant

/**
 * Initializes RevenueCat for the current store and sets the entitlement used by this helper.
 * Call this once during application startup with the platform-specific RevenueCat API key.
 */
fun initPayment(
    apiKey: String,
    isDebug: Boolean,
    subscriptionName: String
) {
    Purchases.logLevel = if (isDebug) LogLevel.DEBUG else LogLevel.INFO
    if (!Purchases.isConfigured) {
        Purchases.configure(apiKey)
    }
    Payment.setSubscriptionName(subscriptionName)
}

/**
 * RevenueCat operations and the small subscription model used by JchuComponents paywalls.
 * The SDK performs the store-specific work through Google Play Billing on Android and StoreKit
 * on iOS; this API can be called from shared Kotlin code.
 */
object Payment {
    private var subscriptionName: String = ""

    private val _billingError = MutableStateFlow<PurchasesError?>(null)
    val billingError: StateFlow<PurchasesError?> = _billingError.asStateFlow()

    private val _billingInfo = MutableStateFlow(BillingInfo.empty())
    val billingInfo: StateFlow<BillingInfo> = _billingInfo.asStateFlow()

    /** The RevenueCat app user ID. RevenueCat must be configured before reading this property. */
    val userId: String
        get() = Purchases.sharedInstance.appUserID

    fun setSubscriptionName(name: String) {
        subscriptionName = name
    }

    /** Loads the current offering and updates [billingInfo] and [billingError]. */
    fun getProducts() {
        loadBillingInfo(
            onSuccess = { _billingInfo.value = it },
            onFailure = { _billingError.value = it }
        )
    }

    /** Loads the current offering and its entitlement details as a suspendable operation. */
    suspend fun loadBillingInfo(): BillingInfo =
        suspendCancellableCoroutine { continuation ->
            loadBillingInfo(
                onSuccess = { info ->
                    _billingInfo.value = info
                    if (continuation.isActive) continuation.resume(info)
                },
                onFailure = { error ->
                    _billingError.value = error
                    if (continuation.isActive) {
                        continuation.resumeWithException(IllegalStateException(error.message))
                    }
                }
            )
        }

    /** Purchases the monthly or annual package currently cached in [billingInfo]. */
    fun purchase(
        type: ProductsType,
        onSuccess: (Boolean, String) -> Unit = { _, _ -> },
        onFailure: (PurchasesError, Boolean) -> Unit = { _, _ -> }
    ) {
        val packageToPurchase =
            billingInfo.value.packages.firstOrNull { it.packageType == type.packageType }
                ?: return

        Purchases.sharedInstance.purchase(
            packageToPurchase = packageToPurchase,
            onError = { error, userCancelled ->
                _billingError.value = error
                onFailure(error, userCancelled)
            },
            onSuccess = { _, customerInfo ->
                activateSubscription(customerInfo, onSuccess)
            }
        )
    }

    /** Purchases a package and returns whether its configured entitlement became active. */
    suspend fun purchaseResult(type: ProductsType): Pair<Boolean, String> =
        suspendCancellableCoroutine { continuation ->
            val packageToPurchase = billingInfo.value.packages.firstOrNull { it.packageType == type.packageType }
            if (packageToPurchase == null) {
                continuation.resumeWithException(
                    IllegalStateException("No ${type.name.lowercase()} package is available in the current offering.")
                )
                return@suspendCancellableCoroutine
            }

            Purchases.sharedInstance.purchase(
                packageToPurchase = packageToPurchase,
                onError = { error, userCancelled ->
                    _billingError.value = error
                    if (continuation.isActive) {
                        val message = if (userCancelled) "Purchase cancelled" else error.message
                        continuation.resumeWithException(IllegalStateException(message))
                    }
                },
                onSuccess = { _, customerInfo ->
                    val result = subscriptionResult(customerInfo)
                    if (continuation.isActive) continuation.resume(result)
                }
            )
        }

    /** Restores purchases associated with the current App Store or Play Store account. */
    fun restorePurchases(onRestored: (Boolean, String) -> Unit = { _, _ -> }) {
        Purchases.sharedInstance.restorePurchases(
            onError = { error -> _billingError.value = error },
            onSuccess = { customerInfo ->
                val (active, productId) = subscriptionResult(customerInfo)
                onRestored(active, productId)
            }
        )
    }

    suspend fun restorePurchasesResult(): Pair<Boolean, String> =
        suspendCancellableCoroutine { continuation ->
            Purchases.sharedInstance.restorePurchases(
                onError = { error ->
                    _billingError.value = error
                    if (continuation.isActive) {
                        continuation.resumeWithException(IllegalStateException(error.message))
                    }
                },
                onSuccess = { customerInfo ->
                    if (continuation.isActive) continuation.resume(subscriptionResult(customerInfo))
                }
            )
        }

    /** Checks the configured entitlement without refreshing products. */
    fun isSubscriptionActive(onChecked: (Boolean, String) -> Unit = { _, _ -> }) {
        Purchases.sharedInstance.getCustomerInfo(
            onError = { error -> _billingError.value = error },
            onSuccess = { customerInfo ->
                val (active, productId) = subscriptionResult(customerInfo)
                onChecked(active, productId)
            }
        )
    }

    /**
     * Switches between the monthly and annual packages. Play Store replacement settings are
     * ignored by StoreKit; RevenueCat applies the native store's subscription rules on iOS.
     */
    fun upgradeDownGrade(
        onSuccess: (Boolean, String) -> Unit = { _, _ -> },
        onFailure: (PurchasesError, Boolean) -> Unit = { _, _ -> }
    ) {
        Purchases.sharedInstance.getCustomerInfo(
            onError = { error ->
                _billingError.value = error
                onFailure(error, false)
            },
            onSuccess = { customerInfo ->
                val entitlement = customerInfo.entitlements[subscriptionName]
                val currentProductId = entitlement?.productIdentifier ?: return@getCustomerInfo
                val currentPackage =
                    billingInfo.value.packages.firstOrNull {
                        productMatches(it, currentProductId)
                    } ?: return@getCustomerInfo
                val targetType =
                    when (currentPackage.packageType) {
                        PackageType.MONTHLY -> PackageType.ANNUAL
                        PackageType.ANNUAL -> PackageType.MONTHLY
                        else -> return@getCustomerInfo
                    }
                val targetPackage =
                    billingInfo.value.packages.firstOrNull { it.packageType == targetType }
                        ?: return@getCustomerInfo

                Purchases.sharedInstance.purchase(
                    packageToPurchase = targetPackage,
                    oldProductId = currentProductId,
                    onError = { error, userCancelled ->
                        _billingError.value = error
                        onFailure(error, userCancelled)
                    },
                    onSuccess = { _, updatedInfo -> activateSubscription(updatedInfo, onSuccess) }
                )
            }
        )
    }

    private fun loadBillingInfo(
        onSuccess: (BillingInfo) -> Unit,
        onFailure: (PurchasesError) -> Unit
    ) {
        Purchases.sharedInstance.getOfferings(
            onError = onFailure,
            onSuccess = { offerings ->
                val packages = offerings.current?.availablePackages.orEmpty()
                if (packages.isEmpty()) {
                    onSuccess(BillingInfo.empty())
                    return@getOfferings
                }

                Purchases.sharedInstance.getCustomerInfo(
                    onError = onFailure,
                    onSuccess = { customerInfo ->
                        onSuccess(
                            BillingInfo(
                                info = buildSubscriptionInfo(customerInfo, packages),
                                packages = packages,
                                products = buildSubscriptionProducts(packages)
                            )
                        )
                    }
                )
            }
        )
    }

    private fun buildSubscriptionProducts(packages: List<Package>): List<Product> {
        val monthly = packages.firstOrNull { it.packageType == PackageType.MONTHLY }
        val annual = packages.firstOrNull { it.packageType == PackageType.ANNUAL }
        if (monthly == null || annual == null) return emptyList()

        val monthlyPrice = monthly.storeProduct.price.amountMicros / 1_000_000.0
        val annualPrice = annual.storeProduct.price.amountMicros / 1_000_000.0
        val annualPricePerMonth = annualPrice / 12
        val savings = if (monthlyPrice > 0.0) 100 - (annualPricePerMonth / monthlyPrice * 100) else 0.0

        return listOf(
            Product(
                isMonthly = true,
                price = monthly.storeProduct.price.formatted,
                priceConversion = null,
                saveAmount = null,
                storeProduct = monthly.storeProduct
            ),
            Product(
                isMonthly = false,
                price = annual.storeProduct.price.formatted,
                priceConversion = annualPricePerMonth.roundTo(2).toString(),
                saveAmount = savings.roundToLong().toString(),
                storeProduct = annual.storeProduct
            )
        )
    }

    private fun buildSubscriptionInfo(
        customerInfo: com.revenuecat.purchases.kmp.models.CustomerInfo,
        packages: List<Package>
    ): SubscriptionInfo {
        val entitlement = customerInfo.entitlements[subscriptionName]
        val entitlementProductId = entitlement?.productIdentifier
        val currentPackage = packages.firstOrNull { entitlementProductId != null && productMatches(it, entitlementProductId) }
        val isPromotional = customerInfo.activeSubscriptions.any { it in PaymentProducts.promos }
        val renewalType =
            when {
                currentPackage?.packageType == PackageType.MONTHLY -> SubscriptionsType.MONTHLY
                currentPackage?.packageType == PackageType.ANNUAL -> SubscriptionsType.YEARLY
                isPromotional -> SubscriptionsType.PROMO
                else -> SubscriptionsType.NONE
            }

        return SubscriptionInfo(
            renewalType = renewalType,
            expireDate = entitlement?.expirationDateMillis?.let(::formatDate).orEmpty(),
            promotional = isPromotional,
            state =
                when {
                    entitlement?.isActive == true -> SubscriptionState.ACTIVE
                    entitlement?.willRenew == true -> SubscriptionState.INACTIVE_UNTIL_RENEWAL
                    else -> SubscriptionState.NONE
                },
            managementUrl = customerInfo.managementUrlString
        )
    }

    private fun activateSubscription(
        customerInfo: com.revenuecat.purchases.kmp.models.CustomerInfo,
        onActiveSubscription: (Boolean, String) -> Unit
    ) {
        val entitlement = customerInfo.entitlements[subscriptionName]
        onActiveSubscription(entitlement?.isActive == true, entitlement?.productIdentifier.orEmpty())
    }

    private fun subscriptionResult(customerInfo: com.revenuecat.purchases.kmp.models.CustomerInfo): Pair<Boolean, String> {
        val entitlement = customerInfo.entitlements[subscriptionName]
        return if (entitlement?.isActive == true) {
            true to entitlement.productIdentifier
        } else {
            false to ""
        }
    }

    private fun productMatches(
        packageInfo: Package,
        productIdentifier: String
    ): Boolean {
        val storeProductId = packageInfo.storeProduct.id
        return storeProductId == productIdentifier || storeProductId.substringBefore(':') == productIdentifier
    }

    private fun formatDate(epochMillis: Long): String {
        val date = Instant.fromEpochMilliseconds(epochMillis).toLocalDateTime(TimeZone.currentSystemDefault())
        val hour = (date.hour % 12).let { if (it == 0) 12 else it }
        val period = if (date.hour < 12) "AM" else "PM"
        return "${date.day.toString().padStart(2, '0')}/${(date.month.ordinal + 1).toString().padStart(2, '0')}/${date.year} " +
            "${hour.toString().padStart(2, '0')}:${date.minute.toString().padStart(2, '0')} $period"
    }

    private fun Double.roundTo(places: Int): Double {
        val factor = 10.0.pow(places.toDouble())
        return kotlin.math.round(this * factor) / factor
    }

    enum class ProductsType(
        val type: String,
        val packageType: PackageType
    ) {
        ANNUAL(type = "\$rc_annual", packageType = PackageType.ANNUAL),
        MONTHLY(type = "\$rc_monthly", packageType = PackageType.MONTHLY)
    }

    object PaymentProducts {
        val promos =
            listOf(
                "rc_promo_pro_daily",
                "rc_promo_pro_three_day",
                "rc_promo_pro_weekly",
                "rc_promo_pro_monthly",
                "rc_promo_pro_three_month",
                "rc_promo_pro_six_month",
                "rc_promo_pro_yearly",
                "rc_promo_pro_lifetime"
            )
    }
}
