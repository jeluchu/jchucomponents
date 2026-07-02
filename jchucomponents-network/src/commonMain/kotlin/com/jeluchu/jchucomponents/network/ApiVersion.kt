package com.jeluchu.jchucomponents.network

public enum class ApiVersion(public val path: String = "") {
    V1(path = "v1"),
    V2(path = "v2"),
    V3(path = "v3"),
    V4(path = "v4"),
    V5(path = "v5"),
    NONE
}
