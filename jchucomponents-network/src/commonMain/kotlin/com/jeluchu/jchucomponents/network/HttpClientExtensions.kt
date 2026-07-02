package com.jeluchu.jchucomponents.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

public suspend inline fun <reified T> HttpClient.getRequest(
    url: String
): T = get(url).body()

public suspend inline fun <reified T> HttpClient.getRequest(
    endpoint: ApiEndpoint
): T = get(endpoint.path()).body()

public suspend inline fun <reified T> HttpClient.postRequest(
    url: String,
    payload: Any,
    headers: Map<String, String> = emptyMap()
): T = post(url) {
    headers.forEach { (key, value) -> header(key, value) }
    setBody(payload)
}.body()

public suspend inline fun <reified T> HttpClient.postRequest(
    endpoint: ApiEndpoint,
    payload: Any? = null,
    headers: Map<String, String> = emptyMap()
): T = post(endpoint.path()) {
    headers.forEach { (key, value) -> header(key, value) }
    setBody(payload)
}.body()

public suspend inline fun <reified T> HttpClient.putRequest(
    url: String,
    payload: Any,
    headers: Map<String, String> = emptyMap()
): T = put(url) {
    headers.forEach { (key, value) -> header(key, value) }
    setBody(payload)
}.body()

public suspend inline fun <reified T> HttpClient.putRequest(
    endpoint: ApiEndpoint,
    payload: Any,
    headers: Map<String, String> = emptyMap()
): T = put(endpoint.path()) {
    headers.forEach { (key, value) -> header(key, value) }
    setBody(payload)
}.body()

public suspend inline fun <reified T> HttpClient.uploadMultipartRequest(
    url: String,
    formData: MultiPartFormDataContent,
    headers: Map<String, String> = emptyMap()
): T = post(url) {
    headers.forEach { (key, value) -> header(key, value) }
    setBody(formData)
}.body()

public suspend inline fun <reified T> HttpClient.uploadMultipartRequest(
    endpoint: ApiEndpoint,
    formData: MultiPartFormDataContent,
    headers: Map<String, String> = emptyMap()
): T = post(endpoint.path()) {
    headers.forEach { (key, value) -> header(key, value) }
    setBody(formData)
}.body()

public fun ApiEndpoint.path(): String =
    listOf(version.path, "$endpoint.$format")
        .filter(String::isNotBlank)
        .joinToString(separator = "/")
