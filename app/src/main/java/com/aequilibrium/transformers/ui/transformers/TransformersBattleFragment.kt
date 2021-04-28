package com.aequilibrium.transformers.ui.transformers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.aequilibrium.transformers.R
import com.aequilibrium.transformers.data.model.Transformer
import com.aequilibrium.transformers.databinding.TransformersBattleFragmentBinding
import com.aequilibrium.transformers.ui.common.BaseFragment
import com.aequilibrium.transformers.utils.Constants.DRAW
import com.aequilibrium.transformers.utils.Constants.OPTIMUS_PRIME
import com.aequilibrium.transformers.utils.Constants.PREDAKING
import com.aequilibrium.transformers.utils.Constants.TEAM_AUTOBOTS
import com.aequilibrium.transformers.utils.Constants.TEAM_DECEPTICONS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransformersBattleFragment : BaseFragment() {

    private lateinit var binding: TransformersBattleFragmentBinding

    private val args: TransformersBattleFragmentArgs by navArgs()

    private var battleResult: StringBuilder = StringBuilder()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TransformersBattleFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        battle(args.transformers)
    }

    private fun battle(transformers: Array<Transformer>) {
        var autobots: ArrayList<Transformer> = ArrayList()
        var decepticons: ArrayList<Transformer> = ArrayList()
        var winningTeam: String = TEAM_AUTOBOTS

        for (transformer in transformers) {
            if (transformer.team == TEAM_AUTOBOTS) {
                autobots.add(transformer)
            } else {
                decepticons.add(transformer)
            }
        }

        var autobotsIndex: Int = autobots.size - 1
        var decepticonsIndex: Int = decepticons.size - 1

        while (autobots.size > 0 && decepticons.size > 0) {

            battleResult.appendLine(
                getString(
                    R.string.result_team_vs_team,
                    getTeam(TEAM_AUTOBOTS),
                    autobots[autobotsIndex].name,
                    getTeam(TEAM_DECEPTICONS),
                    decepticons[decepticonsIndex].name
                )
            )

            if (isOptimusPrimeOrPredaking(autobots[autobotsIndex]) && !isOptimusPrimeOrPredaking(
                    decepticons[decepticonsIndex]
                )
            ) {
                // autobot won automatically
                battleOutput(
                    autobots,
                    decepticons,
                    autobotsIndex,
                    decepticonsIndex,
                    OPTIMUS_PRIME_OR_PREDAKING
                )
                decepticonsIndex--
                winningTeam = autobots[autobotsIndex].team
            } else if (!isOptimusPrimeOrPredaking(autobots[autobotsIndex]) && isOptimusPrimeOrPredaking(
                    decepticons[decepticonsIndex]
                )
            ) {
                // decepticon won automatically
                battleOutput(
                    decepticons,
                    autobots,
                    decepticonsIndex,
                    autobotsIndex,
                    OPTIMUS_PRIME_OR_PREDAKING
                )
                autobotsIndex--
                winningTeam = decepticons[decepticonsIndex].team
            } else if (isOptimusPrimeOrPredaking(autobots[autobotsIndex]) && isOptimusPrimeOrPredaking(
                    decepticons[decepticonsIndex]
                )
            ) {
                // both TEAMS were destroyed
                battleResult.appendLine(getString(R.string.result_both_teams_destroyed))
                    .appendLine()
                autobots.clear()
                decepticons.clear()
                winningTeam = DRAW
            } else {
                when {
                    autobots[autobotsIndex].courage <= 4 && autobots[autobotsIndex].strength + 3 <= decepticons[decepticonsIndex].strength -> {
                        // autobot ran away
                        battleOutput(
                            autobots, decepticons, autobotsIndex, decepticonsIndex, RAN_AWAY
                        )
                        autobotsIndex--
                        winningTeam = decepticons[decepticonsIndex].team
                    }
                    decepticons[decepticonsIndex].courage <= 4 && decepticons[decepticonsIndex].strength + 3 <= autobots[autobotsIndex].strength -> {
                        // decepticon ran away
                        battleOutput(
                            decepticons, autobots, decepticonsIndex, autobotsIndex, RAN_AWAY
                        )
                        decepticonsIndex--
                        winningTeam = autobots[autobotsIndex].team
                    }
                    else -> {
                        when {
                            autobots[autobotsIndex].skill >= decepticons[decepticonsIndex].skill + 3 -> {
                                // autobot won by skill
                                battleOutput(
                                    autobots, decepticons, autobotsIndex, decepticonsIndex, SKILL
                                )
                                decepticonsIndex--
                                winningTeam = autobots[autobotsIndex].team
                            }
                            autobots[autobotsIndex].skill + 3 <= decepticons[decepticonsIndex].skill -> {
                                // decepticon won by skill
                                battleOutput(
                                    decepticons, autobots, decepticonsIndex, autobotsIndex, SKILL
                                )
                                autobotsIndex--
                                winningTeam = decepticons[decepticonsIndex].team
                            }
                            else -> {
                                when {
                                    autobots[autobotsIndex].overallRating() > decepticons[decepticonsIndex].overallRating() -> {
                                        // autobot won by overall rating
                                        battleOutput(
                                            autobots, decepticons, autobotsIndex,
                                            decepticonsIndex, OVERALL_RATING
                                        )
                                        decepticonsIndex--
                                        winningTeam = autobots[autobotsIndex].team
                                    }
                                    autobots[autobotsIndex].overallRating() < decepticons[decepticonsIndex].overallRating() -> {
                                        // decepticon won by overall rating
                                        battleOutput(
                                            decepticons, autobots, decepticonsIndex,
                                            autobotsIndex, OVERALL_RATING
                                        )
                                        autobotsIndex--
                                        winningTeam = decepticons[decepticonsIndex].team
                                    }
                                    else -> {
                                        // both were destroyed
                                        battleResult.appendLine(getString(R.string.result_both_destroyed))
                                            .appendLine()
                                        autobots.remove(autobots[autobotsIndex])
                                        decepticons.remove(decepticons[decepticonsIndex])
                                        autobotsIndex--
                                        decepticonsIndex--
                                        winningTeam = DRAW
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        var winningTeamMessage: StringBuilder = StringBuilder()
        if (winningTeam == TEAM_AUTOBOTS || winningTeam == TEAM_DECEPTICONS) {
            winningTeamMessage.appendLine(
                getString(
                    R.string.result_winning_team_message,
                    getTeam(winningTeam)
                )
            )
            var loosingTeamMessage: StringBuilder = StringBuilder()
            loosingTeamMessage.appendLine(
                getString(
                    R.string.result_loosing_team_message, if (winningTeam == TEAM_AUTOBOTS) {
                        getTeam(TEAM_DECEPTICONS)
                    } else {
                        getTeam(TEAM_AUTOBOTS)
                    }
                )
            )
            getWinningAndLoosingMessages(
                autobots,
                winningTeam,
                TEAM_AUTOBOTS,
                winningTeamMessage,
                loosingTeamMessage
            )
            getWinningAndLoosingMessages(
                decepticons,
                winningTeam,
                TEAM_DECEPTICONS,
                winningTeamMessage,
                loosingTeamMessage
            )

            battleResult.appendLine(winningTeamMessage).appendLine()
            battleResult.appendLine(loosingTeamMessage).appendLine()
        } else {
            battleResult.append(DRAW)
        }

        binding.resultTextView.text = battleResult
    }

    private fun isOptimusPrimeOrPredaking(transformer: Transformer): Boolean {
        return transformer.name == OPTIMUS_PRIME || transformer.name == PREDAKING
    }

    private fun battleOutput(
        winningList: ArrayList<Transformer>, losingList: ArrayList<Transformer>,
        winningIndex: Int, losingIndex: Int, reason: String
    ) {
        when {
            reason == OVERALL_RATING -> {
                battleResult.appendLine(
                    getString(
                        R.string.result_team_name_reason,
                        getTeam(winningList[winningIndex].team),
                        winningList[winningIndex].name,
                        getString(R.string.result_won_overall_rating)
                    )
                )
                    .appendLine()
                losingList.remove(losingList[losingIndex])
            }
            reason == SKILL -> {
                battleResult.appendLine(
                    getString(
                        R.string.result_team_name_reason,
                        getTeam(winningList[winningIndex].team),
                        winningList[winningIndex].name,
                        getString(R.string.result_won_skill)
                    )
                )
                    .appendLine()
                losingList.remove(losingList[losingIndex])
            }
            reason == RAN_AWAY -> {
                battleResult.appendLine(
                    getString(
                        R.string.result_team_name_reason_ran_away,
                        getTeam(winningList[winningIndex].team), winningList[winningIndex].name,
                        losingList[losingIndex].name, getString(R.string.result_won_ran_away)
                    )
                ).appendLine()
            }
            else -> {
                battleResult.appendLine(
                    getString(
                        R.string.result_team_name_reason,
                        getTeam(winningList[winningIndex].team),
                        winningList[winningIndex].name,
                        getString(R.string.result_won_automatically)
                    )
                ).appendLine()
                losingList.remove(losingList[losingIndex])
            }
        }
    }

    private fun getTeam(team: String): String {
        return if (team == TEAM_AUTOBOTS) getString(R.string.result_team_autobots) else getString(R.string.result_team_decepticons)
    }

    private fun getWinningAndLoosingMessages(
        transformers: List<Transformer>, winningTeam: String, currentTeam: String,
        winningTeamMessage: StringBuilder, loosingTeamMessage: StringBuilder
    ) {
        for (transformer in transformers) {
            if (winningTeam == currentTeam) {
                winningTeamMessage.append(transformer.name + if (transformer == transformers[transformers.size - 1]) "" else ", ")
            } else {
                loosingTeamMessage.append(transformer.name + if (transformer == transformers[transformers.size - 1]) "" else ", ")
            }
        }
    }

    companion object {
        private const val OVERALL_RATING = "OVERALL_RATING"
        private const val SKILL = "SKILL"
        private const val RAN_AWAY = "RAN_AWAY"
        private const val OPTIMUS_PRIME_OR_PREDAKING = "OPTIMUS_PRIME_OR_PREDAKING"
    }
}