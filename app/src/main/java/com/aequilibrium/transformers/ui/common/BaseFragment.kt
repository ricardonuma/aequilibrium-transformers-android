package com.aequilibrium.transformers.ui.common

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aequilibrium.transformers.R
import com.aequilibrium.transformers.auth.SessionManager
import com.aequilibrium.transformers.ui.main.MainActivity
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

    fun showLoading() {
        sharedViewModel.loading.postValue(true)
    }

    fun hideLoading() {
        sharedViewModel.loading.postValue(false)
    }
}