package com.aequilibrium.transformers.ui.transformers

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.aequilibrium.transformers.data.model.TransformerRequest
import com.aequilibrium.transformers.data.source.ITransformerRepository

class NewTransformerViewModel @ViewModelInject constructor(
    private val transformerRepository: ITransformerRepository
) : ViewModel() {

    var retryCreateTransformer = MutableLiveData(true)

    fun createTransformer(transformerRequest: TransformerRequest) =
        Transformations.switchMap(retryCreateTransformer) {
            transformerRepository.createTransformer(transformerRequest)
        }
}