package com.jeluchu.jchucomponents.foundation.validators

object CreditCardValidator {
    enum class CardType(
        private val brandName: String,
        private val pattern: Regex
    ) {
        Visa("VISA CARD", "4[0-9]{12}(?:[0-9]{3})?".toRegex()),
        MasterCard("MASTER CARD", "5[1-5][0-9]{14}".toRegex()),
        AmericanExpress("AMERICAN EXPRESS", "3[47][0-9]{13}".toRegex()),
        DinnerClub("DINNER CLUB", "3(?:0[0-5]|[68][0-9])?[0-9]{11}".toRegex()),
        Discover("DISCOVER", "6(?:011|5[0-9]{2})[0-9]{12}".toRegex()),
        Jcb("JCB", "(?:2131|1800|35[0-9]{3})[0-9]{11}".toRegex()),
        Unknown("UNKNOWN", "".toRegex())
        ;

        fun brand(): String = brandName

        fun matches(cardNumber: String): Boolean = this != Unknown && cardNumber.matches(pattern)
    }

    fun getCardType(card: String): CardType {
        val cardNumber = card.replace(" ", "").replace("-", "")
        return CardType.entries.firstOrNull { it.matches(cardNumber) } ?: CardType.Unknown
    }

    fun isValidCard(card: String): Boolean = getCardType(card) != CardType.Unknown
}
