package com.jeluchu.jchucomponents.foundation.extensions.lists

/** Adds every element that is not already present in this mutable list. */
fun <T> MutableList<T>.addAllIfNotExist(elements: Collection<T>) {
    elements.forEach { element ->
        if (!contains(element)) add(element)
    }
}

/** Joins all strings using their lowercase representation. */
fun MutableList<String>.concatenateLowercase(): String = joinToString(separator = "") { it.lowercase() }
