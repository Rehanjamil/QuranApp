package com.quran.rabiulqaloob.ui.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.quran.rabiulqaloob.R
import com.quran.rabiulqaloob.databinding.DialogBookmarkTypeSelectionBinding
import com.quran.rabiulqaloob.utils.showToast

class BookmarkTypeSelectionDialog : DialogFragment() {

    lateinit var binding: DialogBookmarkTypeSelectionBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity())
        binding = DialogBookmarkTypeSelectionBinding.bind(
            layoutInflater.inflate(R.layout.dialog_bookmark_type_selection, null, false)
        )

        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        binding.tvContinue.setOnClickListener {
            binding.rgBookmarkType.checkedRadioButtonId.let {
                    when (binding.rgBookmarkType.checkedRadioButtonId) {
                        binding.rbJuzz.id -> {
                            findNavController().navigate(
                                BookmarkTypeSelectionDialogDirections.actionBookmarkTypeSelectionDialogToJuzzListingFragment()
                                    .setType(1)
                            )
                        }
                        binding.rbSurah.id -> {
                            findNavController().navigate(
                                BookmarkTypeSelectionDialogDirections.actionBookmarkTypeSelectionDialogToSurahListingFragment()
                                    .setType(
                                        1
                                    )
                            )
                        }
                        else -> {
                            requireActivity().showToast(
                                getString(R.string.valid_option)
                            )
                        }
                    }
                }
        }
        binding.tvCancel.setOnClickListener {
            findNavController().navigateUp()
        }
        return dialog

    }

}