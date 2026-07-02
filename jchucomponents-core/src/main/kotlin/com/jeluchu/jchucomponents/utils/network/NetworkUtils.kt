package com.jeluchu.jchucomponents.utils.network

import com.jeluchu.jchucomponents.network.NetworkFileUtils
import io.ktor.client.statement.HttpResponse

@Deprecated(
    message = "Use NetworkFileUtils from jchucomponents-network",
    replaceWith = ReplaceWith(
        expression = "NetworkFileUtils.saveResponseBodyToFile(filePath, response, progress)",
        imports = ["com.jeluchu.jchucomponents.network.NetworkFileUtils"],
    ),
)
public object NetworkUtils {
    public suspend fun saveResponseBodyToFile(
        filePath: String,
        response: HttpResponse,
        progress: (percent: Long) -> Unit,
    ): Unit = NetworkFileUtils.saveResponseBodyToFile(filePath, response, progress)
}
