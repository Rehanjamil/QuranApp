package com.quran.rabiulqaloob.ui.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.quran.rabiulqaloob.BaseDialog
import com.quran.rabiulqaloob.R
import com.quran.rabiulqaloob.databinding.DialogLanguageSelectionBinding
import com.quran.rabiulqaloob.generics.GenericAdapter
import com.quran.rabiulqaloob.generics.ListItemViewModel
import com.quran.rabiulqaloob.models.LanguageModel
import com.quran.rabiulqaloob.ui.MainActivity
import com.quran.rabiulqaloob.utils.Constants
import com.quran.rabiulqaloob.utils.openAndClearActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguageSelectionDialog : BaseDialog() {

    private lateinit var binding: DialogLanguageSelectionBinding
    val args by navArgs<LanguageSelectionDialogArgs>()
    val list = arrayListOf(
        LanguageModel("English", "en").apply {
            isSelected = true
        },
        LanguageModel("Urdu", "ur"),
        LanguageModel("Arabic", "ar"),
        LanguageModel("Turkish", "tr"),
        LanguageModel("Persian", "peo"),
        LanguageModel("French", "fr"),
        LanguageModel("Italian", "it"),
        LanguageModel("Spanish", "es"),
    )

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity())
        binding = DialogLanguageSelectionBinding.bind(
            layoutInflater.inflate(R.layout.dialog_language_selection, null, false)
        )
        binding.clickListener = object : GenericAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int, data: ListItemViewModel) {
                when (data) {
                    is LanguageModel -> {
                        list.forEach {
                            it.isSelected = data.name == it.name
                        }
                    }
                }

            }

        }
        binding.list = list
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        binding.tvContinue.setOnClickListener {
            list.find {
                it.isSelected
            }?.let {
                preferenceHelper.saveLanguage(Constants.LANGUAGE, it.shortCode)
                (requireActivity() as MainActivity).setLanguage(it.shortCode)
                requireActivity().openAndClearActivity(MainActivity::class.java)
            }
        }
        return dialog
    }
}