package com.aequilibrium.transformers.auth

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class ApplicationHeadersInterceptor(private val sessionManager: SessionManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        val accessToken = runBlocking { sessionManager.getToken() }
        requestBuilder.header("Authorization", "Bearer $accessToken")
        requestBuilder.header("Content-Type", "application/json")

        return chain.proceed(requestBuilder.build())
    }
}
