package com.aequilibrium.transformers.ui.transformers

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.aequilibrium.transformers.data.model.TransformerRequest
import com.aequilibrium.transformers.data.source.TransformerRepository

class TransformersViewModel @ViewModelInject constructor(
    private val transformerRepository: TransformerRepository
) : ViewModel() {

    var retryGetTransformers = MutableLiveData<Boolean>(true)
    var retryGetTransformer = MutableLiveData<Boolean>(true)
    var retryCreateTransformer = MutableLiveData<Boolean>(true)
    var retryUpdateTransformer = MutableLiveData<Boolean>(true)
    var retryDeleteTransformer = MutableLiveData<Boolean>(true)

    fun getTransformers() = Transformations.switchMap(retryGetTransformers) {
        transformerRepository.getTransformers()
    }

    fun getTransformer(transformerId: String) = Transformations.switchMap(retryGetTransformer) {
        transformerRepository.getTransformer(transformerId)
    }

    fun createTransformer(transformerRequest: TransformerRequest) =
        Transformations.switchMap(retryCreateTransformer) {
            transformerRepository.createTransformer(transformerRequest)
        }

    fun updateTransformer(transformerRequest: TransformerRequest) =
        Transformations.switchMap(retryUpdateTransformer) {
            transformerRepository.updateTransformer(transformerRequest)
        }

    fun deleteTransformer(transformerId: String) =
        Transformations.switchMap(retryDeleteTransformer) {
            transformerRepository.deleteTransformer(transformerId)
        }
}