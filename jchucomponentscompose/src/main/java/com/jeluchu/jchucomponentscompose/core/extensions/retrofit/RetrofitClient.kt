package com.jeluchu.jchucomponentscompose.core.extensions.retrofit

import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private var CONNECT_TIMEOUT = 60L
    private var READ_TIMEOUT = 60L
    private var WRITE_TIMEOUT = 60L

    fun buildRetrofit(baseUrl: String, converterFactory: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(createClientBuilder())
            .addConverterFactory(converterFactory)
            .build()
    }

    private fun createClientBuilder(): OkHttpClient {

        val logging = HttpLoggingInterceptor()

        val clientBuilder = OkHttpClient.Builder()
            //.addInterceptor(Interceptor())
            .addNetworkInterceptor(logging)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .connectionPool(ConnectionPool(0, 1, TimeUnit.NANOSECONDS))

        return clientBuilder.build()
    }

}

