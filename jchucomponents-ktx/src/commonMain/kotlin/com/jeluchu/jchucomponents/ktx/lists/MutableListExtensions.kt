package com.jeluchu.jchucomponents.ktx.lists

import com.jeluchu.jchucomponents.foundation.extensions.lists.addAllIfNotExist as foundationAddAllIfNotExist
import com.jeluchu.jchucomponents.foundation.extensions.lists.concatenateLowercase as foundationConcatenateLowercase

fun <T> MutableList<T>.addAllIfNotExist(elements: Collection<T>) =
    foundationAddAllIfNotExist(elements)

fun MutableList<String>.concatenateLowercase(): String = foundationConcatenateLowercase()
