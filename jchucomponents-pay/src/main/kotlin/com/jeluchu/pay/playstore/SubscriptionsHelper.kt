package com.jeluchu.pay.playstore

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesResponseListener
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.jeluchu.pay.playstore.enums.BillingState
import com.jeluchu.pay.playstore.extensions.empty
import com.jeluchu.pay.playstore.models.PayInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SubscriptionsHelper(private val context: Context, val subscripttions: List<String>) {

    private lateinit var billingClient: BillingClient
    private lateinit var productsDetails: List<ProductDetails>
    private lateinit var purchase: Purchase

    private val _isSubscribe = MutableStateFlow(false)
    val isSubscribe = _isSubscribe.asStateFlow()

    private val _subsDetails = MutableStateFlow(listOf<PayInfo>())
    val subsDetails = _subsDetails.asStateFlow()

    private val _purchaseStatus = MutableStateFlow(BillingState.BILLING_DISCONNECTED)
    val purchaseStatus = _purchaseStatus.asStateFlow()

    lateinit var onPurchaseCheck: (Boolean, String) -> Unit

    fun initBillings() {
        setUpBillingPurchases()
        queryProduct()
    }

    private fun setUpBillingPurchases() {
        billingClient = BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(
                billingResult: BillingResult
            ) {
                if (billingResult.responseCode ==
                    BillingClient.BillingResponseCode.OK
                ) {
                    _purchaseStatus.value = BillingState.BILLING_CONNECTED
                    queryProduct()
                    reloadPurchase()
                } else {
                    _purchaseStatus.value = BillingState.BILLING_CONNECTION_FAILED
                }
            }

            override fun onBillingServiceDisconnected() {
                _purchaseStatus.value = BillingState.BILLING_LOST_CONNECTION
            }
        })
    }

    fun queryProduct() {
        val queryProductDetailsParams = QueryProductDetailsParams.newBuilder()
            .setProductList(
                subscripttions.map { subId ->
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(subId)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
                }
            )
            .build()

        billingClient.queryProductDetailsAsync(
            queryProductDetailsParams
        ) { _, productDetailsList ->
            if (productDetailsList.isNotEmpty()) {
                productsDetails = productDetailsList
                _subsDetails.value = productDetailsList.map { details ->
                    val price = details.subscriptionOfferDetails?.let { offers ->
                        offers.first()?.pricingPhases?.pricingPhaseList?.first()?.formattedPrice
                    }.orEmpty()

                    PayInfo(
                        id = details.productId,
                        name = details.name,
                        description = details.description,
                        title = details.title,
                        price = price
                    )
                }
            } else _purchaseStatus.value = BillingState.NO_MATCH_PRODUCTS_FOUND
        }
    }

    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode ==
                BillingClient.BillingResponseCode.OK &&
                purchases != null
            ) {
                for (purchase in purchases) {
                    completePurchase(purchase)
                }
            } else if (billingResult.responseCode ==
                BillingClient.BillingResponseCode.USER_CANCELED
            ) {
                _purchaseStatus.value = BillingState.PURCHASE_CANCELLED
            } else {
                _purchaseStatus.value = BillingState.PURCHASE_FAILED
            }
        }

    private fun completePurchase(item: Purchase) {
        purchase = item
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            _isSubscribe.value = true
            onPurchaseCheck(true, purchase.products.firstOrNull().orEmpty())
            _purchaseStatus.value = BillingState.PURCHASE_COMPLETE
        }
    }

    fun launchPurchase(
        productId: String
    ) {
        val subDetails = productsDetails.first { it.productId == productId }
        subDetails.subscriptionOfferDetails?.let { offers ->
            val billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(
                    listOf(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                            .setProductDetails(subDetails)
                            .setOfferToken(offers.first().offerToken)
                            .build()
                    )
                ).build()

            billingClient.launchBillingFlow(context as Activity, billingFlowParams)
        }
    }

    fun upgradePurchase(
        productId: String
    ) {
        val subDetails = productsDetails.first { it.productId == productId }
        subDetails.subscriptionOfferDetails?.let { offers ->
            val billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(
                    listOf(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                            .setProductDetails(subDetails)
                            .setOfferToken(offers.first().offerToken)
                            .build()
                    )
                )
                .setSubscriptionUpdateParams(
                    BillingFlowParams.SubscriptionUpdateParams.newBuilder()
                        .setOldPurchaseToken(purchase.purchaseToken)
                        .setSubscriptionReplacementMode(BillingFlowParams.SubscriptionUpdateParams.ReplacementMode.CHARGE_FULL_PRICE)
                        .build()
                )
                .build()

            billingClient.launchBillingFlow(context as Activity, billingFlowParams)
        }
    }

    private fun reloadPurchase() {
        val queryPurchasesParams = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        billingClient.queryPurchasesAsync(
            queryPurchasesParams,
            purchasesListener
        )
    }

    fun restoreSubscription(
        productId: String,
        onSubscriptionSuccess: (Purchase) -> Unit,
        onSubscriptionFailure: (String) -> Unit,
    ) {
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        ) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases.isNotEmpty()) {
                purchases.forEach { purchase ->
                    if (!purchase.isAcknowledged) {
                        val acknowledgePurchaseParams = AcknowledgePurchaseParams
                            .newBuilder()
                            .setPurchaseToken(purchase.purchaseToken)
                        billingClient.acknowledgePurchase(acknowledgePurchaseParams.build()) { p0 ->
                            if (p0.responseCode == BillingClient.BillingResponseCode.OK) {
                                if (purchase.products.contains(productId)) {
                                    onSubscriptionSuccess(purchase)
                                } else onSubscriptionFailure(p0.debugMessage)
                            } else onSubscriptionFailure(p0.debugMessage)
                        }
                    }
                }
            }
        }
    }

    fun endClientConnection() {
        billingClient.endConnection()
    }

    private val purchasesListener =
        PurchasesResponseListener { _, purchases ->
            if (purchases.isNotEmpty()) {
                purchase = purchases.first()
                _isSubscribe.value = true
                onPurchaseCheck(true, purchase.products.firstOrNull().orEmpty())
                _purchaseStatus.value = BillingState.PREVIOUS_PURCHASE_FOUND
            } else {
                _isSubscribe.value = false
                onPurchaseCheck(false, String.empty())
            }
        }
}

