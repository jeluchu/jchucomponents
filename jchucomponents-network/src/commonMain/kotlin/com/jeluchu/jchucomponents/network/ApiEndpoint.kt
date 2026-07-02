package com.jeluchu.jchucomponents.network

public abstract class ApiEndpoint(public val version: ApiVersion) {
    public abstract val endpoint: String
    public open val format: String = "json"
}
