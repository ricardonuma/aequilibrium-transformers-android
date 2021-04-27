package com.aequilibrium.transformers.ui.transformers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.aequilibrium.transformers.R
import com.aequilibrium.transformers.auth.SessionManager
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

    private var transformerList: ArrayList<Transformer> = ArrayList<Transformer>()

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

//        setupActionButton(R.drawable.toolbar_background_button, R.drawable.ic_baseline_add_24) {
//            battle()
//        }
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
                            addBattleClickListener(object : TransformersAdapter.OnBattleClickListener {
                                override fun onBattleClickListener() {
                                    battle()
                                }
                            })
                        }
                        if (data.transformers != null) {
                            transformerList.addAll(data.transformers.sortedBy { it.rank })
                            setupTransformers(transformerList)
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

    private fun battle() {
        var autobots: ArrayList<Transformer> = ArrayList()
        var decepticons: ArrayList<Transformer> = ArrayList()
        var winningTeam: String = "A"

        for (transformer in transformerList) {
            if (transformer.team == "A") {
                autobots.add(transformer)
            } else {
                decepticons.add(transformer)
            }
        }

        var autobotsIndex: Int = autobots.size - 1
        var decepticonsIndex: Int = decepticons.size - 1

        while (autobots.size > 0 && decepticons.size > 0) {

            Log.d(
                TAG,
                "Autobot: " + autobots[autobotsIndex].name + " Vs Decepticon: " + decepticons[decepticonsIndex].name
            )

            if (isOptimusPrimeOrPredaking(autobots[autobotsIndex]) && !isOptimusPrimeOrPredaking(
                    decepticons[decepticonsIndex]
                )
            ) {
                // decepticon destroyed
                battleOutput(
                    autobots, decepticons, autobotsIndex, decepticonsIndex,
                    winningTeam, " won"
                )
                decepticonsIndex--
                winningTeam = autobots[autobotsIndex].team
            } else if (!isOptimusPrimeOrPredaking(autobots[autobotsIndex]) && isOptimusPrimeOrPredaking(
                    decepticons[decepticonsIndex]
                )
            ) {
                // autobot destroyed
                battleOutput(
                    decepticons, autobots, decepticonsIndex, autobotsIndex,
                    winningTeam, " won"
                )
                autobotsIndex--
                winningTeam = decepticons[decepticonsIndex].team
            } else if (isOptimusPrimeOrPredaking(autobots[autobotsIndex]) && isOptimusPrimeOrPredaking(
                    decepticons[decepticonsIndex]
                )
            ) {
                // both TEAMS are destroyed
                Log.d(TAG, "Both teams are destroyed")
                autobots.clear()
                decepticons.clear()
                winningTeam = "Draw"
            } else {
                when {
                    autobots[autobotsIndex].courage <= 4 && autobots[autobotsIndex].strength + 3 <= decepticons[decepticonsIndex].strength -> {
                        // autobot ran away
                        battleOutput(
                            autobots, decepticons, autobotsIndex, decepticonsIndex,
                            winningTeam, " ran away"
                        )
                        autobotsIndex--
                        winningTeam = decepticons[decepticonsIndex].team
                    }
                    decepticons[decepticonsIndex].courage <= 4 && decepticons[decepticonsIndex].strength + 3 <= autobots[autobotsIndex].strength -> {
                        // decepticon ran away
                        battleOutput(
                            decepticons, autobots, decepticonsIndex, autobotsIndex,
                            winningTeam, " ran away"
                        )
                        decepticonsIndex--
                        winningTeam = autobots[autobotsIndex].team
                    }
                    else -> {
                        when {
                            autobots[autobotsIndex].skill >= decepticons[decepticonsIndex].skill + 3 -> {
                                // decepticon destroyed
                                battleOutput(
                                    autobots, decepticons, autobotsIndex, decepticonsIndex,
                                    winningTeam, " won by skill"
                                )
                                decepticonsIndex--
                                winningTeam = autobots[autobotsIndex].team
                            }
                            autobots[autobotsIndex].skill + 3 <= decepticons[decepticonsIndex].skill -> {
                                // autobot destroyed
                                battleOutput(
                                    decepticons, autobots, decepticonsIndex, autobotsIndex,
                                    winningTeam, " won by skill"
                                )
                                autobotsIndex--
                                winningTeam = decepticons[decepticonsIndex].team
                            }
                            else -> {
                                when {
                                    autobots[autobotsIndex].overallRating() > decepticons[decepticonsIndex].overallRating() -> {
                                        // decepticon destroyed
                                        battleOutput(
                                            autobots, decepticons, autobotsIndex,
                                            decepticonsIndex, winningTeam, " won by overall rating"
                                        )
                                        decepticonsIndex--
                                        winningTeam = autobots[autobotsIndex].team
                                    }
                                    autobots[autobotsIndex].overallRating() < decepticons[decepticonsIndex].overallRating() -> {
                                        // autobot destroyed
                                        battleOutput(
                                            decepticons, autobots, decepticonsIndex,
                                            autobotsIndex, winningTeam, " won by overall rating"
                                        )
                                        autobotsIndex--
                                        winningTeam = decepticons[decepticonsIndex].team
                                    }
                                    else -> {
                                        // both are destroyed
                                        Log.d(
                                            TAG,
                                            "Both are destroyed"
                                        )
                                        autobots.remove(autobots[autobotsIndex])
                                        decepticons.remove(decepticons[decepticonsIndex])
                                        autobotsIndex--
                                        decepticonsIndex--
                                        winningTeam = "Draw"
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        var winningTeamMessage: String = "Winning team ($winningTeam): "
        var loosingTeamMessage: String =
            "Survivors from the losing team (" + if (winningTeam == "A") {
                "D"
            } else {
                "A"
            } + "): "

        for (autobot in autobots) {
            if (winningTeam == "A") {
                winningTeamMessage += autobot.name + if (autobot == autobots[autobots.size - 1]) "" else ", "
            } else {
                loosingTeamMessage += autobot.name + if (autobot == autobots[autobots.size - 1]) "" else ", "
            }
        }

        for (decepticon in decepticons) {
            if (winningTeam == "D") {
                winningTeamMessage += decepticon.name + if (decepticon == decepticons[decepticons.size - 1]) "" else ", "
            } else {
                loosingTeamMessage += decepticon.name + if (decepticon == decepticons[decepticons.size - 1]) "" else ", "
            }
        }

        Log.d(
            TAG,
            winningTeamMessage
        )
        Log.d(
            TAG,
            loosingTeamMessage
        )
    }

    private fun isOptimusPrimeOrPredaking(transformer: Transformer): Boolean {
        return transformer.name == "Optimus Prime" || transformer.name == "Predaking"
    }

    private fun battleOutput(
        winningList: ArrayList<Transformer>, losingList: ArrayList<Transformer>,
        winningIndex: Int, losingIndex: Int, winningTeam: String, message: String
    ) {
        if (message.contains("overall rating") || message.contains("skill")) {
            Log.d(
                TAG,
                winningList[winningIndex].team + ": " + winningList[winningIndex].name + message
            )
            losingList.remove(losingList[losingIndex])
        } else if (message.contains("ran away")) {
            Log.d(
                TAG,
                winningList[winningIndex].team + ": " + winningList[winningIndex].name + " won as " +
                        losingList[losingIndex].name + " ran away"
            )
        } else {
            Log.d(
                TAG,
                winningList[winningIndex].team + ": " + winningList[winningIndex].name + " won as " +
                        losingList[losingIndex].name + " ran away"
            )
            losingList.remove(losingList[losingIndex])
        }
    }

    companion object {
        private val TAG = "BATTLE"
    }
}