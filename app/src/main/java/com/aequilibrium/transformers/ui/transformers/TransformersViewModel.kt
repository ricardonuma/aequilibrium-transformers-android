package com.aequilibrium.transformers.ui.transformers

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.aequilibrium.transformers.data.source.ITransformerRepository

class TransformersViewModel @ViewModelInject constructor(
    private val transformerRepository: ITransformerRepository
) : ViewModel() {

    var retryGetTransformers = MutableLiveData(true)
    var retryGetTransformer = MutableLiveData(true)

    fun getTransformers() = Transformations.switchMap(retryGetTransformers) {
        transformerRepository.getTransformers()
    }

    fun getTransformer(transformerId: String) = Transformations.switchMap(retryGetTransformer) {
        transformerRepository.getTransformer(transformerId)
    }
}