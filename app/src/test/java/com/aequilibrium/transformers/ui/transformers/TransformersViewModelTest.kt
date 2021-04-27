package com.aequilibrium.transformers.ui.transformers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aequilibrium.transformers.CoroutinesTestRule
import com.aequilibrium.transformers.data.model.Transformer
import com.aequilibrium.transformers.data.source.FakeTransformersRepository
import com.aequilibrium.transformers.getOrAwaitValue
import org.hamcrest.core.IsNot
import org.hamcrest.core.IsNull
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TransformersViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private lateinit var repository: FakeTransformersRepository
    private lateinit var viewModel: TransformersViewModel

    @Before
    fun setUp() {
        repository = FakeTransformersRepository(coroutinesTestRule.testDispatcher)
        viewModel = TransformersViewModel(repository)
    }

    @Test
    fun getTransformers_returnsResourceSuccess() {
        val transformer1 = Transformer("id", "name", "A", 1, 1, 1, 1, 1, 1, 1, 1, "team_icon")
        val transformer2 = Transformer("id", "name", "A", 1, 1, 1, 1, 1, 1, 1, 1, "team_icon")
        val response = ArrayList<Transformer>()
        response.add(transformer1)
        response.add(transformer2)
        repository.setResponse(response)
        val resource = viewModel.getTransformers().getOrAwaitValue()
        assertThat(resource.data, IsNot(IsNull()))
    }

    @Test
    fun getTransformers_returnsResourceError() {
        repository.setResponse(ArrayList())
        val resource = viewModel.getTransformers().getOrAwaitValue()
        assertThat(resource.data, IsNull())
    }

    @Test
    fun getTransformer_returnsResourceSuccess() {
        val transformer = Transformer("id", "name", "A", 1, 1, 1, 1, 1, 1, 1, 1, "team_icon")
        repository.setResponse(transformer)
        val id = "id"
        val resource = viewModel.getTransformer(id).getOrAwaitValue()
        assertThat(resource.data, IsNot(IsNull()))
    }

    @Test
    fun getTransformer_returnsResourceError() {
        repository.setResponse(null)
        val id = "id"
        val resource = viewModel.getTransformer(id).getOrAwaitValue()
        assertThat(resource.data, IsNull())
    }
}