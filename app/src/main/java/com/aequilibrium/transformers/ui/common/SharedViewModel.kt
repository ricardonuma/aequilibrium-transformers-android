package com.aequilibrium.transformers.ui.common

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.aequilibrium.transformers.data.source.ITokenRepository

class SharedViewModel @ViewModelInject constructor(private val tokenRepository: ITokenRepository) :
    ViewModel() {

    var loading = MutableLiveData(false)
    var tokenRetry = MutableLiveData(true)

    fun getToken() = Transformations.switchMap(tokenRetry) {
        tokenRepository.getToken()
    }
}
