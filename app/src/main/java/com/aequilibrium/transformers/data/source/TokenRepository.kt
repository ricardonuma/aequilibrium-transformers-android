package com.aequilibrium.transformers.data.source

import androidx.lifecycle.liveData
import com.aequilibrium.transformers.data.model.Resource
import com.aequilibrium.transformers.data.source.remote.TokenRemoteDataSource
import com.aequilibrium.transformers.di.modules.IoDispatcher
import com.aequilibrium.transformers.utils.NoConnectivityException
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TokenRepository @Inject constructor(
    private val remoteDataSource: TokenRemoteDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ITokenRepository {

    override fun getToken() = liveData(ioDispatcher) {

        emit(Resource.Loading())
        try {
            val response = remoteDataSource.getToken()
            val token = response.body()
            if (response.isSuccessful && token != null) {
                emit(Resource.Success(token.string()))
            } else {
                emit(Resource.Error(response?.message(), null))
            }
        } catch (ex: NoConnectivityException) {
            emit(Resource.Error(ex.message, null))
        } catch (ex: Exception) {
            emit(Resource.Error(null, null))
        }
    }
}

