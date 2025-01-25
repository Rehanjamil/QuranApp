package com.quran.rabiulqaloob.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.quran.rabiulqaloob.BaseFragment
import com.quran.rabiulqaloob.R
import com.quran.rabiulqaloob.databinding.FragmentHomeBinding
import com.quran.rabiulqaloob.ui.viewmodels.PrayerViewModel
import com.quran.rabiulqaloob.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale


@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding
    private val prayerViewModel: PrayerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        setUpClickListener()
        setUpUi()
        return binding.root
    }

    private fun setUpUi() {
        prayerViewModel.startUpdatingPrayerTime { stringBuilder, s ->
            binding.tvPrayerName.text = s
            binding.tvPrayerTimeRemaining.text = stringBuilder
            binding.tvIslamicDate.text = getIslamicDate()
            binding.tvDate.text = getDate()
        }
        preferenceHelper.getInt(Constants.CURRENT_READING_INDEX, -1).let { position ->
            if (position != -1) {
                val string = preferenceHelper.getString(
                    Constants.SURAH_NAME,
                    binding.tvSurahReading.text.toString()
                )
                binding.tvSurahReading.text = string
                binding.tvContinueReading.text = getString(R.string.resume_reading)
            } else {

                binding.tvContinueReading.text = getString(R.string.start_reading)
            }

            binding.tvContinueReading.setOnClickListener {
                val directions = HomeFragmentDirections.actionHomeFragmentToReaderFragment()
                    .setPosition(
                        position
                    )
                findNavController().navigate(
                    directions
                )
            }
        }

        binding.ivSettings.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToSettingsBottomSheet()
            )
        }
    }


    private fun getDate(): String {
        return SimpleDateFormat("EEEE, d MMMM", Locale.getDefault()).format(
            Calendar.getInstance().time
        )
    }

    private fun getIslamicDate(): String {
        val currentDate = HijrahDate.now()
        val factor = preferenceHelper.getLong(Constants.DATE_ADJUSTMENT, 0L)
        val adjustedDate = currentDate.plus(
            factor, ChronoUnit.DAYS
        )

        return Constants.hijrahMonthNames[adjustedDate[ChronoField.MONTH_OF_YEAR] - 1] + " " + adjustedDate.format(
            DateTimeFormatter.ofPattern("dd")
        )
    }

    private fun setUpClickListener() {
        binding.cvJuzz.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_juzzListingFragment
            )
        }
        binding.cvSurah.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_surahListingFragment
            )
        }
        binding.cvGotoPage.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_gotoPageDialog
            )
        }
        binding.cvBookmark.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_bookmarkListingFragment
            )
        }
        binding.cvDateDetails.setOnClickListener {
            findNavController()
                .navigate(
                    R.id.action_homeFragment_to_prayerTimesFragment
                )
        }
    }

}