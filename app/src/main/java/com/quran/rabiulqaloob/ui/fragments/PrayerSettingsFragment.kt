package com.quran.rabiulqaloob.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.batoulapps.adhan.CalculationMethod
import com.quran.rabiulqaloob.BaseFragment
import com.quran.rabiulqaloob.databinding.FragmentPrayerSettingsBinding
import com.quran.rabiulqaloob.generics.GenericAdapter
import com.quran.rabiulqaloob.generics.ListItemViewModel
import com.quran.rabiulqaloob.models.LanguageModel
import com.quran.rabiulqaloob.utils.Constants

class PrayerSettingsFragment : BaseFragment() {

    lateinit var binding: FragmentPrayerSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        if (::binding.isInitialized.not()) {
            binding = FragmentPrayerSettingsBinding.inflate(layoutInflater)
            val dateAdjustmentList = arrayListOf(
                LanguageModel(
                    name = "Two Days Ago",
                    "-2"
                ).apply {
                    isSelected = preferenceHelper.getLong(
                        Constants.DATE_ADJUSTMENT,
                        0L
                    ) == shortCode.toLong()
                    if (isSelected) {
                        binding.selectedAdjustment = name
                    }
                },
                LanguageModel(
                    name = "One Day Ago",
                    "-1"
                ).apply {
                    isSelected = preferenceHelper.getLong(
                        Constants.DATE_ADJUSTMENT,
                        0L
                    ) == shortCode.toLong()
                    if (isSelected) {
                        binding.selectedAdjustment = name
                    }
                },
                LanguageModel(
                    name = "None",
                    "0"
                ).apply {
                    isSelected = preferenceHelper.getLong(
                        Constants.DATE_ADJUSTMENT,
                        0L
                    ) == shortCode.toLong()
                    if (isSelected) {
                        binding.selectedAdjustment = name
                    }
                },
                LanguageModel(
                    name = "One Day Ahead",
                    "1"
                ).apply {
                    isSelected = preferenceHelper.getLong(
                        Constants.DATE_ADJUSTMENT,
                        0L
                    ) == shortCode.toLong()
                    if (isSelected) {
                        binding.selectedAdjustment = name
                    }
                },
                LanguageModel(
                    name = "Two Days Ahead",
                    "2"
                ).apply {
                    isSelected = preferenceHelper.getLong(
                        Constants.DATE_ADJUSTMENT,
                        0L
                    ) == shortCode.toLong()
                    if (isSelected) {
                        binding.selectedAdjustment = name
                    }
                },
            )
            binding.dateAdjustmentList = dateAdjustmentList
            binding.dateAdjustmentClick = object : GenericAdapter.OnItemClickListener {
                override fun onClick(view: View, position: Int, data: ListItemViewModel) {
                    when (
                        data
                    ) {
                        is LanguageModel -> {
                            dateAdjustmentList.forEach {
                                it.isSelected = data.name == it.name
                            }

                            preferenceHelper.setLong(
                                Constants.DATE_ADJUSTMENT,
                                data.shortCode.toLong()
                            )

                            binding.selectedAdjustment = data.name
                        }
                    }

                }

            }
            binding.isDateAdjustmentExpanded = false
            val calculationMethodList = arrayListOf<LanguageModel>(
                LanguageModel(
                    name = "Muslim World League",
                    shortCode = CalculationMethod.MUSLIM_WORLD_LEAGUE.name
                ).apply {
                    isSelected = preferenceHelper.getString(
                        Constants.CALCULATION_METHOD,
                        CalculationMethod.MUSLIM_WORLD_LEAGUE.name
                    ) == shortCode
                    if (isSelected) {
                        binding.selectedCalculationMethod = name
                    }
                },
                LanguageModel(
                    name = "Egyptian Survey Authority",
                    shortCode = CalculationMethod.EGYPTIAN.name
                ).apply {
                    isSelected = preferenceHelper.getString(
                        Constants.CALCULATION_METHOD,
                        CalculationMethod.MUSLIM_WORLD_LEAGUE.name
                    ) == shortCode
                    if (isSelected) {
                        binding.selectedCalculationMethod = name
                    }
                },
                LanguageModel(
                    name = "Umm Al-Qurra University",
                    shortCode = CalculationMethod.UMM_AL_QURA.name
                ).apply {
                    isSelected = preferenceHelper.getString(
                        Constants.CALCULATION_METHOD,
                        CalculationMethod.MUSLIM_WORLD_LEAGUE.name
                    ) == shortCode
                    if (isSelected) {
                        binding.selectedCalculationMethod = name
                    }
                },
                LanguageModel(
                    name = "Karachi Univ. of Islamic Sciences",
                    shortCode = CalculationMethod.KARACHI.name
                ).apply {
                    isSelected = preferenceHelper.getString(
                        Constants.CALCULATION_METHOD,
                        CalculationMethod.MUSLIM_WORLD_LEAGUE.name
                    ) == shortCode
                    if (isSelected) {
                        binding.selectedCalculationMethod = name
                    }
                },
                LanguageModel(
                    name = "Islamic Society of North America",
                    CalculationMethod.NORTH_AMERICA.name
                ).apply {
                    isSelected = preferenceHelper.getString(
                        Constants.CALCULATION_METHOD,
                        CalculationMethod.MUSLIM_WORLD_LEAGUE.name
                    ) == shortCode
                    if (isSelected) {
                        binding.selectedCalculationMethod = name
                    }
                },
                LanguageModel(
                    name = "Majlis Ugama Islam",
                    shortCode = CalculationMethod.SINGAPORE.name
                ).apply {
                    isSelected = preferenceHelper.getString(
                        Constants.CALCULATION_METHOD,
                        CalculationMethod.MUSLIM_WORLD_LEAGUE.name
                    ) == shortCode
                    if (isSelected) {
                        binding.selectedCalculationMethod = name
                    }
                },
                LanguageModel(
                    name = "Kuwait Ministry of Awqaf",
                    CalculationMethod.KUWAIT.name
                ).apply {
                    isSelected = preferenceHelper.getString(
                        Constants.CALCULATION_METHOD,
                        CalculationMethod.MUSLIM_WORLD_LEAGUE.name
                    ) == shortCode
                    if (isSelected) {
                        binding.selectedCalculationMethod = name
                    }
                },
            )
            binding.calculationMethodList = calculationMethodList
            binding.calculationMethodClick = object : GenericAdapter.OnItemClickListener {
                override fun onClick(view: View, position: Int, data: ListItemViewModel) {
                    when (
                        data
                    ) {
                        is LanguageModel -> {
                            calculationMethodList.forEach {
                                it.isSelected = data.name == it.name
                            }

                            preferenceHelper.setString(
                                Constants.CALCULATION_METHOD,
                                data.shortCode
                            )
                            binding.selectedCalculationMethod = data.name
                        }
                    }

                }

            }
            val timeFormatList = arrayListOf<LanguageModel>(
                LanguageModel(
                    name = "24 Hour",
                    shortCode = "HH:mm"
                ).apply {
                    isSelected = preferenceHelper.getString(
                        Constants.TIME_FORMAT,
                        "hh:mm a"
                    ) == shortCode
                    if (isSelected) {
                        binding.selectedTimeFormat = name
                    }
                },
                LanguageModel(
                    name = "12 Hour(am/pm)",
                    shortCode = "hh:mm a"
                ).apply {
                    isSelected = preferenceHelper.getString(
                        Constants.TIME_FORMAT,
                        "hh:mm a"
                    ) == shortCode
                    if (isSelected) {
                        binding.selectedTimeFormat = name
                    }
                }
            )

            binding.timeFormatList = timeFormatList
            binding.timeFormatClick = object : GenericAdapter.OnItemClickListener {
                override fun onClick(view: View, position: Int, data: ListItemViewModel) {
                    when (
                        data
                    ) {
                        is LanguageModel -> {
                            timeFormatList.forEach {
                                it.isSelected = data.name == it.name
                            }

                            preferenceHelper.setString(
                                Constants.TIME_FORMAT,
                                data.shortCode
                            )
                            binding.selectedTimeFormat = data.name
                        }
                    }

                }

            }
            binding.isDateAdjustmentExpanded = false
            binding.isCalculationMethodExpanded = false
            binding.isTimeFormatExpanded = false
            setUpClickListeners()
        }
        return binding.root
    }

    private fun setUpClickListeners() {
        binding.ivHijriDateAdjustment.setOnClickListener {
            binding.isDateAdjustmentExpanded = binding.isDateAdjustmentExpanded?.not()
        }
        binding.ivCalculationMethod.setOnClickListener {
            binding.isCalculationMethodExpanded = binding.isCalculationMethodExpanded?.not()
        }
        binding.ivTimeFormat.setOnClickListener {
            binding.isTimeFormatExpanded = binding.isTimeFormatExpanded?.not()
        }

        binding.ivArrowBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}