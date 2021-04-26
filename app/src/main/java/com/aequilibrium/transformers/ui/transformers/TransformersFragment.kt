package com.aequilibrium.transformers.ui.transformers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.aequilibrium.transformers.data.model.Resource
import com.aequilibrium.transformers.data.model.Transformer
import com.aequilibrium.transformers.databinding.TransformersFragmentBinding
import com.aequilibrium.transformers.ui.common.BaseFragment
import com.aequilibrium.transformers.utils.Constants.offlineExceptionError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class TransformersFragment : BaseFragment() {

    private lateinit var binding: TransformersFragmentBinding
    private val transformersViewModel: TransformersViewModel by viewModels()

    private lateinit var adapter: TransformersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TransformersFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val accessToken = runBlocking { sessionManager.getToken() }
        if (accessToken.isNullOrEmpty()) {
            getToken { getTransformers() }
        } else {
            getTransformers()
        }
        binding.transformersRefreshLayout.setOnRefreshListener {
            getTransformers()
            binding.transformersRefreshLayout.isRefreshing = false
        }

        binding.addFab.setOnClickListener {
            findNavController().navigate(
                TransformersFragmentDirections.actionTransformersFragmentToNewTransformerFragment()
            )
        }
    }

    private fun getTransformers() {
        transformersViewModel.getTransformers().observe(viewLifecycleOwner) { it ->
            when (it) {
                is Resource.Loading -> showLoading()
                is Resource.Error -> {
                    hideLoading()
                    if (it.message != null && it.message.contains(offlineExceptionError)) {
                        showNoInternetDialog()
                    } else {
                        showErrorDialog()
                    }
                }
                is Resource.Success -> {
                    hideLoading()
                    it.data?.let { data ->
                        adapter = TransformersAdapter(arrayListOf(), requireContext()).apply {
                            addTransformerClickListener(object :
                                TransformersAdapter.OnTransformerClickListener {
                                override fun onTransformerClick(transformer: Transformer) {
                                    findNavController().navigate(
                                        TransformersFragmentDirections.actionTransformersFragmentToEditTransformerFragment(
                                            transformer
                                        )
                                    )
                                }
                            })
                        }
                        if (data.transformers != null) {
                            setupTransformers(data.transformers)
                        }
                    }
                }
            }
        }
    }

    private fun setupTransformers(list: List<Transformer>) {
        binding.transformersRecyclerView.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(context)
        binding.transformersRecyclerView.adapter = adapter
        adapter.addTransformerList(list)
    }
}