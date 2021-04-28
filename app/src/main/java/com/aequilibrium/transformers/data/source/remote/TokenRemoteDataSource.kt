package com.aequilibrium.transformers.data.source.remote

import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRemoteDataSource @Inject constructor(private val apiInterface: ApiInterface) {

    suspend fun getToken(): Response<ResponseBody> {
        return apiInterface.getToken()
    }
}