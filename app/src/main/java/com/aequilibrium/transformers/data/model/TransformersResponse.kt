package com.aequilibrium.transformers.data.model


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransformersResponse(
    @Json(name = "transformers") val transformers: List<Transformer>?
) : BaseResponse(), Parcelable