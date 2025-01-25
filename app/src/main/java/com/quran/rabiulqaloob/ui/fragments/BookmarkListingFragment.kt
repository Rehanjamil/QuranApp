package com.quran.rabiulqaloob.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.quran.rabiulqaloob.BaseFragment
import com.quran.rabiulqaloob.R
import com.quran.rabiulqaloob.databinding.FragmentBookmarkListingBinding
import com.quran.rabiulqaloob.generics.GenericAdapter
import com.quran.rabiulqaloob.generics.ListItemViewModel
import com.quran.rabiulqaloob.models.BookmarkModel
import com.quran.rabiulqaloob.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookmarkListingFragment : BaseFragment() {

    lateinit var binding: FragmentBookmarkListingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentBookmarkListingBinding.inflate(layoutInflater)
        val bookmarkList = preferenceHelper.getBookMarkList(Constants.BOOKMARK)
        binding.list = bookmarkList
        binding.clickListener = object :GenericAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int, data: ListItemViewModel) {
                (data as BookmarkModel).let {
                    when(view.id) {
                        R.id.ivBookmark -> {
                            bookmarkList.removeAt(position)
                            binding.list = bookmarkList
                            lifecycleScope.launch(Dispatchers.IO) {
                                preferenceHelper.saveList(
                                    bookmarkList,
                                    Constants.BOOKMARK
                                )
                            }
                        }
                        else -> {
                            val direction =
                                BookmarkListingFragmentDirections.actionBookmarkListingFragmentToReaderFragment()
                                    .setPosition(
                                        data.pageNo - 2
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

        binding.fbAddBookmark.setOnClickListener {
            findNavController()
                .navigate(
                    BookmarkListingFragmentDirections.actionBookmarkListingFragmentToBookmarkTypeSelectionDialog()
                )
        }
        binding.ivArrowBack.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }
}