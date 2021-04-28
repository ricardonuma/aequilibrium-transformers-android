package com.aequilibrium.transformers.ui.transformers

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aequilibrium.transformers.R
import com.aequilibrium.transformers.data.model.Resource
import com.aequilibrium.transformers.data.model.Transformer
import com.aequilibrium.transformers.data.model.TransformerRequest
import com.aequilibrium.transformers.databinding.EditTransformerFragmentBinding
import com.aequilibrium.transformers.databinding.TransformerStatsLayoutBinding
import com.aequilibrium.transformers.ui.common.BaseFragment
import com.aequilibrium.transformers.utils.Constants
import com.aequilibrium.transformers.utils.Constants.TEAM_AUTOBOTS
import com.aequilibrium.transformers.utils.Constants.TEAM_DECEPTICONS
import com.google.android.material.slider.Slider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt

@AndroidEntryPoint
class EditTransformerFragment : BaseFragment() {

    private lateinit var binding: EditTransformerFragmentBinding
    private val editTransformerViewModel: EditTransformerViewModel by viewModels()
    private val args: EditTransformerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EditTransformerFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadTransformer(args.transformer)

        setupName()

        setupRadioGroupBackground()
        binding.autobotsRadioButton.setOnClickListener {
            setupRadioButtonBackground(binding.autobotsRadioButton, binding.decepticonsRadioButton)
        }
        binding.decepticonsRadioButton.setOnClickListener {
            setupRadioButtonBackground(binding.decepticonsRadioButton, binding.autobotsRadioButton)
        }
        enableSaveButton(validateChanges())
    }

    private fun setupName() {
        binding.nameEditText.addTextChangedListener { enableSaveButton(validateChanges()) }
    }

    private fun setupRadioGroupBackground() {
        val radioGroupBgDrawable: Drawable? =
            ContextCompat.getDrawable(requireContext(), R.drawable.switch_light_grey)
        radioGroupBgDrawable?.alpha = 15
        binding.teamRadioGroup.background = radioGroupBgDrawable
    }

    private fun setupRadioButtonBackground(
        clickedRadioButton: RadioButton,
        otherRadioButton: RadioButton
    ) {
        clickedRadioButton.isChecked = true
        clickedRadioButton.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                if (clickedRadioButton == binding.autobotsRadioButton) {
                    R.color.colorRed
                } else {
                    R.color.colorPurple
                }
            )
        )
        otherRadioButton.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorSwitchText
            )
        )
        enableSaveButton(validateChanges())
    }

    private fun loadTransformer(transformer: Transformer) {
        binding.nameEditText.setText(transformer.name)
        if (args.transformer.team == TEAM_AUTOBOTS) {
            setupRadioButtonBackground(binding.autobotsRadioButton, binding.decepticonsRadioButton)
        } else {
            setupRadioButtonBackground(binding.decepticonsRadioButton, binding.autobotsRadioButton)
        }

        setupStats(transformer)

        binding.updateButton.setOnClickListener {
            val accessToken = runBlocking { sessionManager.getToken() }
            if (accessToken.isEmpty()) {
                getToken { updateTransformer(transformer) }
            } else {
                updateTransformer(transformer)
            }
        }
        binding.deleteButton.setOnClickListener {
            showConfirmDialog(R.string.transformer_delete_confirm_dialog_title) {
                val accessToken = runBlocking { sessionManager.getToken() }
                if (accessToken.isEmpty()) {
                    getToken { deleteTransformer(transformer.id) }
                } else {
                    deleteTransformer(transformer.id)
                }
            }
        }
    }

    private fun setupStats(transformer: Transformer) {
        setupStatsLayouts(
            binding.strengthLayout,
            R.string.transformer_strength,
            transformer.strength,
        )
        setupStatsLayouts(
            binding.intelligenceLayout,
            R.string.transformer_intelligence,
            transformer.intelligence
        )
        setupStatsLayouts(binding.speedLayout, R.string.transformer_speed, transformer.speed)
        setupStatsLayouts(
            binding.enduranceLayout,
            R.string.transformer_endurance,
            transformer.endurance
        )
        setupStatsLayouts(binding.rankLayout, R.string.transformer_rank, transformer.rank)
        setupStatsLayouts(binding.courageLayout, R.string.transformer_courage, transformer.courage)
        setupStatsLayouts(
            binding.firepowerLayout,
            R.string.transformer_firepower,
            transformer.firepower
        )
        setupStatsLayouts(binding.skillLayout, R.string.transformer_skill, transformer.skill)
    }


    private fun setupStatsLayouts(
        layout: TransformerStatsLayoutBinding,
        statsResource: Int,
        statsValue: Int
    ) {
        layout.statsTextView.text = getString(
            R.string.transformer_stats_values,
            getString(statsResource),
            statsValue.toString()
        )
        layout.slider.value = statsValue.toFloat()
        layout.slider.addOnChangeListener { slider, _, _ ->
            onStatsValueChange(slider, layout.statsTextView, statsResource)
        }
    }

    private fun onStatsValueChange(slider: Slider, valueTextView: TextView, statsResource: Int) {
        valueTextView.apply {
            text = getString(
                R.string.transformer_stats_values,
                getString(statsResource),
                getSliderValueLabel(slider.value)
            )
            requestLayout()
        }
        enableSaveButton(validateChanges())
    }

    private fun getSliderValueLabel(value: Float): String {
        val valueRoundedToTenth = value.roundToInt()
        return valueRoundedToTenth.toString()
    }

    private fun validateChanges(): Boolean =
        binding.nameEditText.text.toString() != args.transformer.name ||
                if (binding.autobotsRadioButton.isChecked) {
                    args.transformer.team != TEAM_AUTOBOTS
                } else {
                    args.transformer.team != TEAM_DECEPTICONS
                }
                ||
                binding.strengthLayout.slider.value.toInt() != args.transformer.strength ||
                binding.intelligenceLayout.slider.value.toInt() != args.transformer.intelligence ||
                binding.speedLayout.slider.value.toInt() != args.transformer.speed ||
                binding.enduranceLayout.slider.value.toInt() != args.transformer.endurance ||
                binding.rankLayout.slider.value.toInt() != args.transformer.rank ||
                binding.courageLayout.slider.value.toInt() != args.transformer.courage ||
                binding.firepowerLayout.slider.value.toInt() != args.transformer.firepower ||
                binding.skillLayout.slider.value.toInt() != args.transformer.skill

    private fun enableSaveButton(enableButton: Boolean) {
        if (enableButton) {
            binding.updateButton.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.background_round_white)
            binding.updateButton.isEnabled = true
        } else {
            val disabledButtonBgDrawable: Drawable? =
                ContextCompat.getDrawable(requireContext(), R.drawable.background_round_white)
            disabledButtonBgDrawable?.alpha = 100
            binding.updateButton.background = disabledButtonBgDrawable
            binding.updateButton.isEnabled = false
        }
    }

    private fun updateTransformer(transformer: Transformer) {
        val transformerUpdated = TransformerRequest(
            transformer.id,
            binding.nameEditText.text.toString(),
            if (binding.autobotsRadioButton.isChecked) TEAM_AUTOBOTS else TEAM_DECEPTICONS,
            binding.strengthLayout.slider.value.toInt(),
            binding.intelligenceLayout.slider.value.toInt(),
            binding.speedLayout.slider.value.toInt(),
            binding.enduranceLayout.slider.value.toInt(),
            binding.rankLayout.slider.value.toInt(),
            binding.courageLayout.slider.value.toInt(),
            binding.firepowerLayout.slider.value.toInt(),
            binding.skillLayout.slider.value.toInt()
        )
        editTransformerViewModel.updateTransformer(transformerUpdated)
            .observe(viewLifecycleOwner) { it ->
                when (it) {
                    is Resource.Loading -> showLoading()
                    is Resource.Error -> {
                        hideLoading()
                        if (it.message != null && it.message.contains(Constants.offlineExceptionError)) {
                            showNoInternetDialog()
                        } else {
                            showErrorDialog()
                        }
                    }
                    is Resource.Success -> {
                        findNavController().navigate(EditTransformerFragmentDirections.actionEditTransformersFragmentToTransformersFragment())
                    }
                }
            }
    }

    private fun deleteTransformer(transformerId: String) {
        editTransformerViewModel.deleteTransformer(transformerId)
            .observe(viewLifecycleOwner) { it ->
                when (it) {
                    is Resource.Loading -> showLoading()
                    is Resource.Error -> {
                        hideLoading()
                        if (it.message != null && it.message.contains(Constants.offlineExceptionError)) {
                            showNoInternetDialog()
                        } else {
                            showErrorDialog()
                        }
                    }
                    is Resource.Success -> {
                        findNavController().navigate(EditTransformerFragmentDirections.actionEditTransformersFragmentToTransformersFragment())
                    }
                }
            }
    }
}