package com.aequilibrium.transformers.ui.transformers

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.aequilibrium.transformers.data.model.TransformerRequest
import com.aequilibrium.transformers.data.source.ITransformerRepository

class EditTransformerViewModel @ViewModelInject constructor(
    private val transformerRepository: ITransformerRepository
) : ViewModel() {

    var retryUpdateTransformer = MutableLiveData(true)
    var retryDeleteTransformer = MutableLiveData(true)

    fun updateTransformer(transformerRequest: TransformerRequest) =
        Transformations.switchMap(retryUpdateTransformer) {
            transformerRepository.updateTransformer(transformerRequest)
        }

    fun deleteTransformer(transformerId: String) =
        Transformations.switchMap(retryDeleteTransformer) {
            transformerRepository.deleteTransformer(transformerId)
        }
}