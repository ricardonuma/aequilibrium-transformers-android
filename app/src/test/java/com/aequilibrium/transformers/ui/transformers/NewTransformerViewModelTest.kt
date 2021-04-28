package com.aequilibrium.transformers.ui.transformers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aequilibrium.transformers.CoroutinesTestRule
import com.aequilibrium.transformers.data.model.Resource
import com.aequilibrium.transformers.data.model.TransformerRequest
import com.aequilibrium.transformers.data.source.FakeTransformersRepository
import com.aequilibrium.transformers.getOrAwaitValue
import org.hamcrest.core.IsEqual
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NewTransformerViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private lateinit var repository: FakeTransformersRepository
    private lateinit var viewModel: NewTransformerViewModel

    @Before
    fun setUp() {
        repository = FakeTransformersRepository(coroutinesTestRule.testDispatcher)
        viewModel = NewTransformerViewModel(repository)
    }

    @Test
    fun createTransformer_returnsResourceSuccess() {
        val resource = Resource.Success(null)
        repository.setResource(resource)
        val request = TransformerRequest("id", "name", "A", 1, 1, 1, 1, 1, 1, 1, 1)
        val isUserTermsAccepted = viewModel.createTransformer(request).getOrAwaitValue()
        assertThat(isUserTermsAccepted, IsEqual(resource))
    }

    @Test
    fun createTransformer_returnsResourceError() {
        val resource = Resource.Error("", null)
        repository.setResource(resource)
        val request = TransformerRequest("id", "name", "A", 1, 1, 1, 1, 1, 1, 1, 1)
        val isUserTermsAccepted = viewModel.createTransformer(request).getOrAwaitValue()
        assertThat(isUserTermsAccepted, IsEqual(resource))
    }
}