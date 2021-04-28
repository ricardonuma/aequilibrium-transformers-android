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
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.aequilibrium.transformers.R
import com.aequilibrium.transformers.data.model.Resource
import com.aequilibrium.transformers.data.model.TransformerRequest
import com.aequilibrium.transformers.databinding.NewTransformerFragmentBinding
import com.aequilibrium.transformers.databinding.TransformerStatsLayoutBinding
import com.aequilibrium.transformers.ui.common.BaseFragment
import com.aequilibrium.transformers.utils.Constants
import com.google.android.material.slider.Slider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt

@AndroidEntryPoint
class NewTransformerFragment : BaseFragment() {

    private lateinit var binding: NewTransformerFragmentBinding
    private val newTransformerViewModel: NewTransformerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NewTransformerFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupName()

        setupRadioGroupBackground()
        setupRadioButtonBackground(binding.autobotsRadioButton, binding.decepticonsRadioButton)
        binding.autobotsRadioButton.setOnClickListener {
            setupRadioButtonBackground(binding.autobotsRadioButton, binding.decepticonsRadioButton)
        }
        binding.decepticonsRadioButton.setOnClickListener {
            setupRadioButtonBackground(binding.decepticonsRadioButton, binding.autobotsRadioButton)
        }

        setupStats()

        binding.createButton.setOnClickListener {
            val accessToken = runBlocking { sessionManager.getToken() }
            if (accessToken.isNullOrEmpty()) {
                getToken { createTransformer() }
            } else {
                createTransformer()
            }
        }
    }

    private fun setupName() {
        binding.nameEditText.addTextChangedListener { validate() }
    }

    private fun setupRadioGroupBackground() {
        var radioGroupBgDrawable: Drawable? =
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
    }

    private fun setupStats() {
        setupStatsLayouts(binding.strengthLayout, R.string.transformer_strength)
        setupStatsLayouts(binding.intelligenceLayout, R.string.transformer_intelligence)
        setupStatsLayouts(binding.speedLayout, R.string.transformer_speed)
        setupStatsLayouts(binding.enduranceLayout, R.string.transformer_endurance)
        setupStatsLayouts(binding.rankLayout, R.string.transformer_rank)
        setupStatsLayouts(binding.courageLayout, R.string.transformer_courage)
        setupStatsLayouts(binding.firepowerLayout, R.string.transformer_firepower)
        setupStatsLayouts(binding.skillLayout, R.string.transformer_skill)
    }

    private fun setupStatsLayouts(
        layout: TransformerStatsLayoutBinding,
        statsResource: Int,
        statsValue: Int = resources.getString(R.string.transformer_default_value).toInt()
    ) {
        layout.statsTextView.text = getString(
            R.string.transformer_stats_values,
            getString(statsResource),
            statsValue.toString()
        )
        layout.slider.value = statsValue.toFloat()
        layout.slider.addOnChangeListener { slider, _, _ ->
            onValueChange(slider, layout.statsTextView, statsResource)
        }
    }

    private fun onValueChange(slider: Slider, valueTextView: TextView, statsResource: Int) {
        valueTextView.apply {
            text = getString(
                R.string.transformer_stats_values,
                getString(statsResource),
                getSliderValueLabel(slider.value)
            )
            requestLayout()
        }
    }

    private fun getSliderValueLabel(value: Float): String {
        val valueRoundedToTenth = value.roundToInt()
        return valueRoundedToTenth.toString()
    }

    private fun validate() {
        if (binding.nameEditText.text.isNullOrEmpty()) {
            enableCreateTransformerButton(false)
        } else {
            enableCreateTransformerButton(true)
        }
    }

    private fun enableCreateTransformerButton(enableButton: Boolean) {
        if (enableButton) {
            binding.createButton.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.background_round_white)
            binding.createButton.isEnabled = true
        } else {
            val disabledButtonBgDrawable: Drawable? =
                ContextCompat.getDrawable(requireContext(), R.drawable.background_round_white)
            disabledButtonBgDrawable?.alpha = 100
            binding.createButton.background = disabledButtonBgDrawable
            binding.createButton.isEnabled = false
        }
    }

    private fun createTransformer() {
        val newTransformer = TransformerRequest(
            "",
            binding.nameEditText.text.toString(),
            if (binding.autobotsRadioButton.isChecked) "A" else "D",
            binding.strengthLayout.slider.value.toInt(),
            binding.intelligenceLayout.slider.value.toInt(),
            binding.speedLayout.slider.value.toInt(),
            binding.enduranceLayout.slider.value.toInt(),
            binding.rankLayout.slider.value.toInt(),
            binding.courageLayout.slider.value.toInt(),
            binding.firepowerLayout.slider.value.toInt(),
            binding.skillLayout.slider.value.toInt()
        )
        newTransformerViewModel.createTransformer(newTransformer).observe(viewLifecycleOwner) { it ->
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
                    findNavController().navigate(NewTransformerFragmentDirections.actionNewTransformersFragmentToTransformersFragment())
                }
            }
        }
    }
}