package com.aequilibrium.transformers.utils

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class NetworkConnectionInterceptor(
    private val context: Context
) : Interceptor {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!Utils.isNetworkAvailable(context)) {
            throw NoConnectivityException()
        }
        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

}

class NoConnectivityException : IOException() {
    override val message: String
        get() = Constants.offlineExceptionError

}