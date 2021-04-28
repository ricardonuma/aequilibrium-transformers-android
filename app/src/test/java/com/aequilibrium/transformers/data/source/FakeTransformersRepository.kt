package com.aequilibrium.transformers.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.aequilibrium.transformers.data.model.Resource
import com.aequilibrium.transformers.data.model.Transformer
import com.aequilibrium.transformers.data.model.TransformerRequest
import com.aequilibrium.transformers.di.modules.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher

class FakeTransformersRepository constructor(@IoDispatcher private val ioDispatcher: CoroutineDispatcher) :
    ITransformerRepository {

    private lateinit var responseTransformers: MutableList<Transformer>
    private var responseTransformer: Transformer? = null
    private lateinit var resource: Resource<out Nothing?>

    override fun getTransformers(): LiveData<Resource<out List<Transformer>?>> =
        liveData(ioDispatcher) {
            if (responseTransformers.isNotEmpty()) {
                return@liveData emit(Resource.Success(responseTransformers))
            } else {
                return@liveData emit(Resource.Error("", null))
            }
        }

    override fun getTransformer(transformerId: String): LiveData<Resource<out Transformer?>> =
        liveData(ioDispatcher) {
            if (responseTransformer != null) {
                return@liveData emit(Resource.Success(responseTransformer))
            } else {
                return@liveData emit(Resource.Error("", null))
            }
        }

    override fun createTransformer(transformerRequest: TransformerRequest): LiveData<Resource<out Nothing?>> =
        liveData(ioDispatcher) {
            return@liveData emit(resource)
        }

    override fun updateTransformer(transformerRequest: TransformerRequest): LiveData<Resource<out Nothing?>> =
        liveData(ioDispatcher) {
            return@liveData emit(resource)
        }

    override fun deleteTransformer(transformerId: String): LiveData<Resource<out Nothing?>> =
        liveData(ioDispatcher) {
            return@liveData emit(resource)
        }

    fun setResponse(response: MutableList<Transformer>) {
        this.responseTransformers = response
    }

    fun setResponse(response: Transformer?) {
        this.responseTransformer = response
    }

    fun setResource(resource: Resource<out Nothing?>) {
        this.resource = resource
    }
}