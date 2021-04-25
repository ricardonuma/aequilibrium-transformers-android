package com.aequilibrium.transformers.data.model

import com.squareup.moshi.Json

open class BaseResponse(
        @Json(name = "success") open var success: Boolean? = null,
        @Json(name = "message") open var message: String? = null
)