package com.aequilibrium.transformers.data.source

import androidx.lifecycle.LiveData
import com.aequilibrium.transformers.data.model.Resource
import com.aequilibrium.transformers.data.model.Transformer
import com.aequilibrium.transformers.data.model.TransformerRequest

interface ITransformerRepository {
    fun getTransformers(): LiveData<Resource<out List<Transformer>?>>
    fun getTransformer(transformerId: String): LiveData<Resource<out Transformer?>>
    fun createTransformer(transformerRequest: TransformerRequest): LiveData<Resource<out Nothing?>>
    fun updateTransformer(transformerRequest: TransformerRequest): LiveData<Resource<out Nothing?>>
    fun deleteTransformer(transformerId: String): LiveData<Resource<out Nothing?>>
}