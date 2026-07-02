package com.jeluchu.jchucomponents.extensions.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

suspend inline fun <reified T> HttpClient.getRequest(
    url: String
): T = get(url).body()

suspend inline fun <reified T> HttpClient.getRequest(
    endpoint: ApiEndpoint
): T = get(path(endpoint)).body()

suspend inline fun <reified T> HttpClient.postRequest(
    url: String,
    payload: Any,
    headers: Map<String, String> = emptyMap()
): T = post(url) {
    headers.forEach { (key, value) -> header(key, value) }
    setBody(payload)
}.body()

suspend inline fun <reified T> HttpClient.postRequest(
    endpoint: ApiEndpoint,
    payload: Any? = null,
    headers: Map<String, String> = emptyMap()
): T = post(path(endpoint)) {
    headers.forEach { (key, value) -> header(key, value) }
    setBody(payload)
}.body()

suspend inline fun <reified T> HttpClient.putRequest(
    url: String,
    payload: Any,
    headers: Map<String, String> = emptyMap()
): T = put(url) {
    headers.forEach { (key, value) -> header(key, value) }
    setBody(payload)
}.body()

suspend inline fun <reified T> HttpClient.putRequest(
    endpoint: ApiEndpoint,
    payload: Any,
    headers: Map<String, String> = emptyMap()
): T = put(path(endpoint)) {
    headers.forEach { (key, value) -> header(key, value) }
    setBody(payload)
}.body()

suspend inline fun <reified T> HttpClient.uploadMultipartRequest(
    url: String,
    formData: MultiPartFormDataContent,
    headers: Map<String, String> = emptyMap()
): T = post(url) {
    headers.forEach { (key, value) -> header(key, value) }
    setBody(formData)
}.body()

suspend inline fun <reified T> HttpClient.uploadMultipartRequest(
    endpoint: ApiEndpoint,
    formData: MultiPartFormDataContent,
    headers: Map<String, String> = emptyMap()
): T = post(path(endpoint)) {
    headers.forEach { (key, value) -> header(key, value) }
    setBody(formData)
}.body()

val path: (ApiEndpoint) -> String = { api ->
    "${api.version.path}/${api.endpoint}.${api.format}"
}