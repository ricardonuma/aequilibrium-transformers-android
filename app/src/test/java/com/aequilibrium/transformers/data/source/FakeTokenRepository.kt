package com.aequilibrium.transformers.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.aequilibrium.transformers.data.model.Resource
import com.aequilibrium.transformers.di.modules.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher

class FakeTokenRepository constructor(@IoDispatcher private val ioDispatcher: CoroutineDispatcher) :
    ITokenRepository {

    private lateinit var response: String

    override fun getToken(): LiveData<Resource<out String>> =
        liveData(ioDispatcher) {
            if (response.isNotEmpty()) {
                return@liveData emit(Resource.Success(response))
            } else {
                return@liveData emit(Resource.Error("", null))
            }
        }

    fun setResponse(response: String) {
        this.response = response
    }
}