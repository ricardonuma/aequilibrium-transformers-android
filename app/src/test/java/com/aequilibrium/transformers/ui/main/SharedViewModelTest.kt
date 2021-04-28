package com.aequilibrium.transformers.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aequilibrium.transformers.CoroutinesTestRule
import com.aequilibrium.transformers.data.source.FakeTokenRepository
import com.aequilibrium.transformers.getOrAwaitValue
import com.aequilibrium.transformers.ui.common.SharedViewModel
import org.hamcrest.core.IsNot
import org.hamcrest.core.IsNull
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class SharedViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private lateinit var repository: FakeTokenRepository
    private lateinit var viewModel: SharedViewModel

    @Before
    fun setUp() {
        repository = FakeTokenRepository(coroutinesTestRule.testDispatcher)
        viewModel = SharedViewModel(repository)
    }

    @Test
    fun getToken_returnsResourceSuccess() {
        repository.setResponse("123456789")
        val resource = viewModel.getToken().getOrAwaitValue()
        Assert.assertThat(resource, IsNot(IsNull()))
    }

    @Test
    fun getToken_returnsResourceError() {
        repository.setResponse("")
        val resource = viewModel.getToken().getOrAwaitValue()
        Assert.assertThat(resource.data, IsNull())
    }
}