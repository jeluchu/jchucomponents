package com.jeluchu.jchucomponents.ktx.lists

fun <T> MutableList<T>.addAllIfNotExist(elements: Collection<T>) {
    for (element in elements) {
        if (!contains(element)) add(element)
    }
}

fun MutableList<String>.concatenateLowercase() : String {
    return this.joinToString("") { s -> s.lowercase() }
}