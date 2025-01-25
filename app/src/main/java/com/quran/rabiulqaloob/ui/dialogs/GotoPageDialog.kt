package com.quran.rabiulqaloob.ui.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.quran.rabiulqaloob.R
import com.quran.rabiulqaloob.databinding.DialogGotoPageBinding
import com.quran.rabiulqaloob.databinding.DownloadingDialogBinding
import com.quran.rabiulqaloob.utils.showToast

class GotoPageDialog : DialogFragment() {

    lateinit var binding: DialogGotoPageBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity())
        binding = DialogGotoPageBinding.bind(
            layoutInflater.inflate(R.layout.dialog_goto_page, null, false)
        )

        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        binding.tvContinue.setOnClickListener {
            binding.edtPageToGo.text.toString().let {
                if(it.isNotEmpty()){
                    if(it.toInt() < 550) {
                        findNavController()
                            .navigate(
                                GotoPageDialogDirections.actionGotoPageDialogToReaderFragment()
                                    .setPosition(
                                        it.toInt().minus(2)
                                    )
                            )
                    } else {
                        requireActivity().showToast(
                            getString(R.string.invalid_page_no)
                        )
                        findNavController().navigateUp()
                    }
                } else {
                    requireActivity().showToast(
                        binding.edtPageToGo.hint.toString()
                    )
                }
            }

        }
        binding.tvCancel.setOnClickListener {
            findNavController().navigateUp()
        }
        return dialog

    }
}