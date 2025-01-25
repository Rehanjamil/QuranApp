package com.quran.rabiulqaloob.ui.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.quran.rabiulqaloob.BaseDialog
import com.quran.rabiulqaloob.R
import com.quran.rabiulqaloob.databinding.DialogCreateBookmarkBinding
import com.quran.rabiulqaloob.models.BookmarkModel
import com.quran.rabiulqaloob.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateBookmarkDialog : BaseDialog() {

    lateinit var binding: DialogCreateBookmarkBinding
    private val args by navArgs<CreateBookmarkDialogArgs>()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity())
        binding = DialogCreateBookmarkBinding.bind(
            layoutInflater.inflate(R.layout.dialog_create_bookmark, null, false)
        )
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        binding.tvContinue.setOnClickListener {
            binding.edtBookmarkName.text.toString().takeIf {
                it.isNotEmpty()
            }?.let {
                val bookmarkModel = BookmarkModel(
                    name = binding.edtBookmarkName.text.toString(),
                    englishName = Constants.juzzListing[args.pageModel.juzzNo.toInt() - 1].juzzName,
                    pageNo = args.pageModel.pageNo.toInt(),
                    type = 0
                )
                val bookmarkList = preferenceHelper.getBookMarkList(
                    Constants.BOOKMARK
                )
                bookmarkList.add(bookmarkModel)
                lifecycleScope.launch(Dispatchers.IO) {
                    preferenceHelper.saveList(
                        bookmarkList,
                        Constants.BOOKMARK
                    )
                }
                findNavController().navigateUp()


            }
        }
        binding.tvCancel.setOnClickListener {
            findNavController().navigateUp()
        }
        setData()
        return dialog
    }

    private fun setData() {
        binding.edtBookmarkName.setText(
            args.pageModel.surahNames
        )
        binding.tvJuzzName.text =
            Constants.juzzListing[args.pageModel.juzzNo.toInt() - 1].juzzArabic
        binding.tvEngJuzzName.text =
            Constants.juzzListing[args.pageModel.juzzNo.toInt() - 1].juzzName
        binding.tvSurahNames.text = args.pageModel.surahNames
        binding.tvPageNo.text = "Page # ${args.pageModel.pageNo}"
    }


}