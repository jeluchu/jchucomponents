package com.jeluchu.jchucomponents.network.http

import com.jeluchu.jchucomponents.network.api.ApiEndpoint
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

suspend inline fun <reified T> HttpClient.getRequest(url: String): T = get(urlString = url).body()

suspend inline fun <reified T> HttpClient.getRequest(endpoint: ApiEndpoint): T = get(urlString = endpoint.path()).body()

suspend inline fun <reified T> HttpClient.postRequest(
    url: String,
    payload: Any,
    headers: Map<String, String> = emptyMap(),
): T =
    post(urlString = url) {
        headers.forEach { (key, value) -> header(key, value) }
        setBody(payload)
    }.body()

suspend inline fun <reified T> HttpClient.postRequest(
    endpoint: ApiEndpoint,
    payload: Any? = null,
    headers: Map<String, String> = emptyMap(),
): T =
    post(urlString = endpoint.path()) {
        headers.forEach { (key, value) -> header(key, value) }
        setBody(payload)
    }.body()

suspend inline fun <reified T> HttpClient.putRequest(
    url: String,
    payload: Any,
    headers: Map<String, String> = emptyMap(),
): T =
    put(urlString = url) {
        headers.forEach { (key, value) -> header(key, value) }
        setBody(payload)
    }.body()

suspend inline fun <reified T> HttpClient.putRequest(
    endpoint: ApiEndpoint,
    payload: Any,
    headers: Map<String, String> = emptyMap(),
): T =
    put(urlString = endpoint.path()) {
        headers.forEach { (key, value) -> header(key, value) }
        setBody(payload)
    }.body()

suspend inline fun <reified T> HttpClient.uploadMultipartRequest(
    url: String,
    formData: MultiPartFormDataContent,
    headers: Map<String, String> = emptyMap(),
): T =
    post(urlString = url) {
        headers.forEach { (key, value) -> header(key, value) }
        setBody(formData)
    }.body()

suspend inline fun <reified T> HttpClient.uploadMultipartRequest(
    endpoint: ApiEndpoint,
    formData: MultiPartFormDataContent,
    headers: Map<String, String> = emptyMap(),
): T =
    post(urlString = endpoint.path()) {
        headers.forEach { (key, value) -> header(key, value) }
        setBody(formData)
    }.body()

fun ApiEndpoint.path(): String =
    listOf(version.path, "$endpoint.$format")
        .filter(predicate = String::isNotBlank)
        .joinToString(separator = "/")
