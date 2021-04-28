package com.aequilibrium.transformers.data.source.remote

import com.aequilibrium.transformers.data.model.Transformer
import com.aequilibrium.transformers.data.model.TransformerRequest
import com.aequilibrium.transformers.data.model.TransformersResponse
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransformerRemoteDataSource @Inject constructor(private val apiInterface: ApiInterface) {

    suspend fun getTransformers(): Response<TransformersResponse> {
        return apiInterface.getTransformers()
    }

    suspend fun getTransformer(transformerId: String): Response<Transformer?> {
        return apiInterface.getTransformer(transformerId)
    }

    suspend fun createTransformer(transformerRequest: TransformerRequest): Response<ResponseBody> {
        return apiInterface.createTransformer(transformerRequest)
    }

    suspend fun updateTransformer(transformerRequest: TransformerRequest): Response<ResponseBody> {
        return apiInterface.updateTransformer(transformerRequest)
    }

    suspend fun deleteTransformer(transformerId: String): Response<ResponseBody> {
        return apiInterface.deleteTransformer(transformerId)
    }
}