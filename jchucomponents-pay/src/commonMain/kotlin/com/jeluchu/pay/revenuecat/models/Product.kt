package com.jeluchu.pay.revenuecat.models

data class Product(
    val isMonthly: Boolean,
    val price: String,
    val priceConversion: String?,
    val saveAmount: String?,
    val storeProduct: RevenueCatStoreProduct
)

expect interface RevenueCatStoreProduct
