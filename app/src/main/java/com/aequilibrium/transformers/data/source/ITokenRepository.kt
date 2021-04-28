package com.aequilibrium.transformers.data.source

import androidx.lifecycle.LiveData
import com.aequilibrium.transformers.data.model.Resource
import okhttp3.ResponseBody

interface ITokenRepository {
    fun getToken(): LiveData<Resource<out String>>
}