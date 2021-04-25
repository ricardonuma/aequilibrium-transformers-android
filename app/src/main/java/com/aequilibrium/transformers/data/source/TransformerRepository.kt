package com.aequilibrium.transformers.data.source

import androidx.lifecycle.liveData
import com.aequilibrium.transformers.data.model.Resource
import com.aequilibrium.transformers.data.model.TransformerRequest
import com.aequilibrium.transformers.data.source.remote.TransformerRemoteDataSource
import com.aequilibrium.transformers.di.modules.IoDispatcher
import com.aequilibrium.transformers.utils.NoConnectivityException
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TransformerRepository @Inject constructor(
    private val remoteDataSource: TransformerRemoteDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ITransformerRepository {

    override fun getTransformers() = liveData(ioDispatcher) {

        emit(Resource.Loading())
        try {
            val response = remoteDataSource.getTransformers()
            val transformersResponse = response.body()
            if (response.isSuccessful && transformersResponse != null) {
                emit(Resource.Success(transformersResponse))
            } else {
                emit(Resource.Error(transformersResponse?.message, null))
            }
        } catch (ex: NoConnectivityException) {
            emit(Resource.Error(ex.message, null))
        } catch (ex: Exception) {
            emit(Resource.Error(null, null))
        }
    }

    override fun getTransformer(transformerId: String) = liveData(ioDispatcher) {

        emit(Resource.Loading())
        try {
            val response = remoteDataSource.getTransformer(transformerId)
            val transformer = response.body()
            if (response.isSuccessful && transformer != null) {
                emit(Resource.Success(transformer))
            } else {
                emit(Resource.Error(response?.message(), null))
            }
        } catch (ex: NoConnectivityException) {
            emit(Resource.Error(ex.message, null))
        } catch (ex: Exception) {
            emit(Resource.Error(null, null))
        }
    }

    override fun createTransformer(transformerRequest: TransformerRequest) =
        liveData(ioDispatcher) {
            emit(Resource.Loading())
            try {
                val response = remoteDataSource.createTransformer(transformerRequest)
                if (response.isSuccessful) {
                    emit(Resource.Success(null))
                } else {
                    val responseBody = response.errorBody()
                    emit(Resource.Error(responseBody?.string(), null))
                }
            } catch (ex: NoConnectivityException) {
                emit(Resource.Error(ex.message, null))
            } catch (ex: Exception) {
                emit(Resource.Error(null, null))
            }
        }

    override fun updateTransformer(transformerRequest: TransformerRequest) =
        liveData(ioDispatcher) {
            emit(Resource.Loading())
            try {
                val response = remoteDataSource.updateTransformer(transformerRequest)
                if (response.isSuccessful) {
                    emit(Resource.Success(null))
                } else {
                    val responseBody = response.errorBody()
                    emit(Resource.Error(responseBody?.string(), null))
                }
            } catch (ex: NoConnectivityException) {
                emit(Resource.Error(ex.message, null))
            } catch (ex: Exception) {
                emit(Resource.Error(null, null))
            }
        }

    override fun deleteTransformer(transformerId: String) = liveData(ioDispatcher) {
        emit(Resource.Loading())
        try {
            val response = remoteDataSource.deleteTransformer(transformerId)
            if (response.isSuccessful) {
                emit(Resource.Success(null))
            } else {
                val responseBody = response.errorBody()
                emit(Resource.Error(responseBody?.string(), null))
            }
        } catch (ex: NoConnectivityException) {
            emit(Resource.Error(ex.message, null))
        } catch (ex: Exception) {
            emit(Resource.Error(null, null))
        }
    }
}

