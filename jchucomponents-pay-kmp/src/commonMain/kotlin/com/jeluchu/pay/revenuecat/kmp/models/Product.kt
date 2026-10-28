package com.jeluchu.pay.revenuecat.kmp.models

import com.revenuecat.purchases.kmp.models.StoreProduct

data class Product(
    val isMonthly: Boolean,
    val price: String,
    val priceConversion: String?,
    val saveAmount: String?,
    val storeProduct: StoreProduct
)
