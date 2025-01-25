package com.quran.rabiulqaloob.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.quran.rabiulqaloob.BaseFragment
import com.quran.rabiulqaloob.R
import com.quran.rabiulqaloob.databinding.FragmentJuzzListingBinding
import com.quran.rabiulqaloob.generics.GenericAdapter
import com.quran.rabiulqaloob.generics.ListItemViewModel
import com.quran.rabiulqaloob.models.BookmarkModel
import com.quran.rabiulqaloob.models.Juzz
import com.quran.rabiulqaloob.utils.Constants
import com.quran.rabiulqaloob.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class JuzzListingFragment : BaseFragment() {
    lateinit var binding: FragmentJuzzListingBinding
    private val args by navArgs<JuzzListingFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        if (::binding.isInitialized.not()) {
            binding = FragmentJuzzListingBinding.inflate(layoutInflater)
            val bookmarkList = preferenceHelper.getBookMarkList(
                Constants.BOOKMARK
            )
            val arrayList = ArrayList(Constants.juzzListing.map { juzz ->
                juzz.isBookMarkType = args.type == 1
                juzz.isBookMarked = bookmarkList.any {
                    it.name == juzz.juzzArabic
                }
                juzz
            })
            binding.list = arrayList
            binding.clickListener = object : GenericAdapter.OnItemClickListener {
                override fun onClick(view: View, position: Int, data: ListItemViewModel) {
                    (data as Juzz).let {
                        when (view.id) {
                            R.id.tvBookmark -> {
                                val state = it.isBookMarked
                                if (state) {
                                    val index = bookmarkList.indexOfFirst {
                                        it.name == data.juzzArabic
                                    }
                                    if (index != -1) {
                                        bookmarkList.removeAt(index)
                                    }
                                    requireActivity().showToast(
                                        "Bookmark removed: ${it.juzzName}"
                                    )
                                } else {
                                    bookmarkList.add(
                                        BookmarkModel(
                                            name = data.juzzArabic,
                                            englishName = data.juzzName,
                                            pageNo = data.pageNo.toInt(),
                                            type = 0
                                        )
                                    )
                                    requireActivity().showToast(
                                        "Successfully Bookmarked: ${it.juzzName}"
                                    )
                                }
                                lifecycleScope.launch(Dispatchers.IO) {
                                    preferenceHelper.saveList(
                                        bookmarkList,
                                        Constants.BOOKMARK
                                    )
                                }
                            }

                            else -> {
                                val direction =
                                    JuzzListingFragmentDirections.actionJuzzListingFragmentToReaderFragment()
                                        .setPosition(
                                            data.pageNo.toInt() - 2
                                        )

                                findNavController()
                                    .navigate(
                                        direction
                                    )
                            }
                        }
                    }
                }

            }
            binding.ivArrowBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
        return binding.root
    }

}