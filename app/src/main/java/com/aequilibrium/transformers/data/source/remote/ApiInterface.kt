package com.aequilibrium.transformers.data.source.remote

import com.aequilibrium.transformers.data.model.Transformer
import com.aequilibrium.transformers.data.model.TransformerRequest
import com.aequilibrium.transformers.data.model.TransformersResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    //transformers
    @GET("allspark")
    suspend fun getToken(): Response<ResponseBody>
    //endregion

    //transformers
    @GET("transformers")
    suspend fun getTransformers(): Response<TransformersResponse>

    @GET("transformers/{transformerId}")
    suspend fun getTransformer(@Path("transformerId") transformerId: String): Response<Transformer>

    @POST("transformers")
    suspend fun createTransformer(@Body transformerRequest: TransformerRequest): Response<ResponseBody>

    @PUT("transformers")
    suspend fun updateTransformer(@Body transformerRequest: TransformerRequest): Response<ResponseBody>

    @DELETE("transformers/{transformerId}")
    suspend fun deleteTransformer(@Path("transformerId") bookingId: String): Response<ResponseBody>
    //endregion
}