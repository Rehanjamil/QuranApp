package com.quran.rabiulqaloob.ui.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.quran.rabiulqaloob.BaseDialog
import com.quran.rabiulqaloob.R
import com.quran.rabiulqaloob.databinding.DialogDownloadRequiredBinding
import com.quran.rabiulqaloob.databinding.DownloadingDialogBinding
import com.quran.rabiulqaloob.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DownloadRequiredDialog : BaseDialog() {
    private lateinit var binding: DialogDownloadRequiredBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogDownloadRequiredBinding.bind(
            layoutInflater.inflate(R.layout.dialog_download_required, null, false)
        )
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        binding.tvContinue.setOnClickListener {
            preferenceHelper.setBol(Constants.RESOURCE_PERMISSION, true)
            findNavController().navigate(
                R.id.action_downloadRequiredDialog_to_downloadingDialog
            )
        }
        return dialog
    }

}