package com.quran.rabiulqaloob.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.quran.rabiulqaloob.BaseFragment
import com.quran.rabiulqaloob.R
import com.quran.rabiulqaloob.databinding.FragmentSurahListingBinding
import com.quran.rabiulqaloob.generics.GenericAdapter
import com.quran.rabiulqaloob.generics.ListItemViewModel
import com.quran.rabiulqaloob.models.BookmarkModel
import com.quran.rabiulqaloob.models.PlayerModel
import com.quran.rabiulqaloob.models.SurahModel
import com.quran.rabiulqaloob.ui.viewmodels.PlayerViewModel
import com.quran.rabiulqaloob.ui.viewmodels.PrayerViewModel
import com.quran.rabiulqaloob.utils.Constants
import com.quran.rabiulqaloob.utils.readJsonFromAssets
import com.quran.rabiulqaloob.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class SurahListingFragment : BaseFragment() {

    lateinit var binding: FragmentSurahListingBinding
    private val args by navArgs<SurahListingFragmentArgs>()
    private val playerViewModel by activityViewModels<PlayerViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (::binding.isInitialized.not()) {
            binding = FragmentSurahListingBinding.inflate(layoutInflater)
            val bookmarkList = preferenceHelper.getBookMarkList(
                Constants.BOOKMARK
            )
            val list = ArrayList(requireActivity().readJsonFromAssets(
                "surah.json"
            ).map { surah ->
                surah.isBookMarkType = args.type == 1
                surah.isBookMarked = bookmarkList.any {
                    it.name == surah.name
                }
                surah
            })
            binding.clickListener = object : GenericAdapter.OnItemClickListener {
                override fun onClick(view: View, position: Int, data: ListItemViewModel) {
                    (data as SurahModel).let {
                        when (view.id) {
                            R.id.tvSurahReading -> {
                                val direction =
                                    SurahListingFragmentDirections.actionSurahListingFragmentToReaderFragment()
                                        .setPosition(
                                            data.pageNo - 2
                                        )

                                findNavController()
                                    .navigate(
                                        direction
                                    )
                            }

                            R.id.tvBookmark -> {
                                val state = it.isBookMarked
                                if (state) {
                                    val index = bookmarkList.indexOfFirst {
                                        it.name == data.name
                                    }
                                    if (index != -1) {
                                        bookmarkList.removeAt(index)
                                    }
                                    requireActivity().showToast(
                                        "Bookmark removed: ${it.englishName}"
                                    )
                                } else {
                                    bookmarkList.add(
                                        BookmarkModel(
                                            name = data.name,
                                            englishName = data.englishName,
                                            pageNo = data.pageNo,
                                            type = 1
                                        )
                                    )
                                    requireActivity().showToast(
                                        "Successfully Bookmarked: ${it.englishName}"
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
                                preferenceHelper.setString(
                                    Constants.SURAH_NUMBER,
                                    it.number.toString()
                                )

                                playerViewModel.setPlayerModel(
                                    PlayerModel(currentSurah = it.number.toString(), isPlayerShown = true)
                                )
                            }
                        }

                    }
                }
            }
            binding.list = list
            binding.ivArrowBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
        return binding.root
    }
}