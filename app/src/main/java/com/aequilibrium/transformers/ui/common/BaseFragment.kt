package com.aequilibrium.transformers.ui.common

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import com.aequilibrium.transformers.R
import com.aequilibrium.transformers.auth.SessionManager
import com.aequilibrium.transformers.data.model.Resource
import com.aequilibrium.transformers.ui.main.MainActivity
import com.aequilibrium.transformers.utils.Constants
import javax.inject.Inject


abstract class BaseFragment : Fragment() {

    @Inject
    lateinit var sessionManager: SessionManager

    val sharedViewModel: SharedViewModel by activityViewModels()
    private var activity: MainActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = requireActivity() as? MainActivity
    }

    fun showNoInternetDialog(onClick: (() -> Unit) = {}) {
        AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(R.string.no_internet_title))
            .setMessage(resources.getString(R.string.no_internet_message))
            .setPositiveButton(resources.getString(R.string.default_ok_text)) { dialog, _ ->
                dialog.cancel()
                onClick.invoke()
            }
            .show()
    }

    fun showErrorDialog(
        @StringRes titleId: Int = R.string.error_dialog_title,
        @StringRes messageId: Int = R.string.error_dialog_message
    ) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(titleId))
            .setMessage(getString(messageId))
            .setPositiveButton(getString(R.string.default_ok_text)) { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    fun showConfirmDialog(title: Int, onClick: (() -> Unit)) {
        AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(title))
            .setPositiveButton(resources.getString(R.string.confirm_dialog_button_yes)) { dialog, _ ->
                dialog.cancel()
                onClick.invoke()
            }
            .setNegativeButton(resources.getString(R.string.confirm_dialog_button_no)) { dialog, _ ->
                dialog.cancel()
            }
            .setCancelable(false)
            .create()
            .show()
    }

    fun showLoading() {
        sharedViewModel.loading.postValue(true)
    }

    fun hideLoading() {
        sharedViewModel.loading.postValue(false)
    }

    fun getToken(function: (() -> Unit)) {
        sharedViewModel.getToken().observe(viewLifecycleOwner) { it ->
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
                    it.data?.let { data ->
                        sessionManager.storeToken(data)
                        function.invoke()
                    }
                }
            }
        }
    }
}