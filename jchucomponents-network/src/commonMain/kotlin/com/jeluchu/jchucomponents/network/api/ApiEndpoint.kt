package com.jeluchu.jchucomponents.network.api

abstract class ApiEndpoint(val version: ApiVersion) {
    abstract val endpoint: String
    open val format: String = "json"
}
