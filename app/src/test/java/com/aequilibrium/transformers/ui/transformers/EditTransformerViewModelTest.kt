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

class EditTransformerViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private lateinit var repository: FakeTransformersRepository
    private lateinit var viewModel: EditTransformerViewModel

    @Before
    fun setUp() {
        repository = FakeTransformersRepository(coroutinesTestRule.testDispatcher)
        viewModel = EditTransformerViewModel(repository)
    }

    @Test
    fun updateTransformer_returnsResourceSuccess() {
        val resource = Resource.Success(null)
        repository.setResource(resource)
        val request = TransformerRequest("id", "name", "A", 1, 1, 1, 1, 1, 1, 1, 1)
        val isUserTermsAccepted = viewModel.updateTransformer(request).getOrAwaitValue()
        assertThat(isUserTermsAccepted, IsEqual(resource))
    }

    @Test
    fun updateTransformer_returnsResourceError() {
        val resource = Resource.Error("", null)
        repository.setResource(resource)
        val request = TransformerRequest("id", "name", "A", 1, 1, 1, 1, 1, 1, 1, 1)
        val isUserTermsAccepted = viewModel.updateTransformer(request).getOrAwaitValue()
        assertThat(isUserTermsAccepted, IsEqual(resource))
    }

    @Test
    fun deleteTransformer_returnsResourceSuccess() {
        val resource = Resource.Success(null)
        repository.setResource(resource)
        val id = "id"
        val isUserTermsAccepted = viewModel.deleteTransformer(id).getOrAwaitValue()
        assertThat(isUserTermsAccepted, IsEqual(resource))
    }

    @Test
    fun deleteTransformer_returnsResourceError() {
        val resource = Resource.Error("", null)
        repository.setResource(resource)
        val id = "id"
        val isUserTermsAccepted = viewModel.deleteTransformer(id).getOrAwaitValue()
        assertThat(isUserTermsAccepted, IsEqual(resource))
    }
}