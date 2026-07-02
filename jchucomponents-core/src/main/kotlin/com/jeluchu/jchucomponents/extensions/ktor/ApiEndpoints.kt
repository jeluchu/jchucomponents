package com.jeluchu.jchucomponents.extensions.ktor

abstract class ApiEndpoint(val version: ApiVersion) {
    abstract val endpoint: String
    open val format: String = "json"
}