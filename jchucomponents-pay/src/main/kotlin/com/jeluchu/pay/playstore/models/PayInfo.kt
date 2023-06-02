package com.jeluchu.pay.playstore.models

import com.jeluchu.pay.playstore.extensions.empty

data class PayInfo(
    val id: String,
    val name: String,
    val description: String,
    val title: String,
    val price: String
) {
    companion object {
        fun empty() = PayInfo(
            id = String.empty(),
            name = String.empty(),
            description = String.empty(),
            title = String.empty(),
            price = String.empty()
        )
    }
}