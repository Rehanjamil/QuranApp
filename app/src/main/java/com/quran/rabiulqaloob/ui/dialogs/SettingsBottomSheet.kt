package com.quran.rabiulqaloob.ui.dialogs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.quran.rabiulqaloob.R
import com.quran.rabiulqaloob.databinding.BottomSheetSettingsBinding
import com.quran.rabiulqaloob.generics.GenericAdapter
import com.quran.rabiulqaloob.generics.ListItemViewModel
import com.quran.rabiulqaloob.models.SettingsModel
import com.quran.rabiulqaloob.models.SettingsType

class SettingsBottomSheet : BottomSheetDialogFragment() {

    private val binding: BottomSheetSettingsBinding by lazy {
        BottomSheetSettingsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding.list = arrayListOf(
            SettingsModel(
                "Languages",
                SettingsType.LANGUAGE
            ),
            SettingsModel(
                "Rate App",
                SettingsType.RATE_APP
            ),
            SettingsModel(
                "Privacy Policy",
                SettingsType.PRIVACY_POLICY
            ),

            )

        binding.clickListener = object : GenericAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int, data: ListItemViewModel) {
                (data as SettingsModel).let {
                    when (it.settingType) {
                        SettingsType.LANGUAGE -> {

                            findNavController().navigate(
                                SettingsBottomSheetDirections.actionSettingsBottomSheetToLanguageSelectionDialog()
                                    .setIsLanguageUpdate(true)
                            )
                        }

                        SettingsType.RATE_APP -> {
                            try {
                                // Open the Play Store page of the current app
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id=$context.packageName")
                                    )
                                )
                            } catch (e: android.content.ActivityNotFoundException) {
                                // If the Play Store app is not available, open the Play Store website
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id=$context.packageName")
                                    )
                                )
                            }
                            findNavController().navigateUp()
                        }

                        SettingsType.PRIVACY_POLICY -> {
                            val builder = CustomTabsIntent.Builder()
                            val customTabsIntent: CustomTabsIntent = builder.build()
                            customTabsIntent.intent.`package` = "com.android.chrome"
                            customTabsIntent.launchUrl(
                                requireContext(),
                                "https://rowwapp.com/terms-of-service/".toUri()
                            )
                            findNavController().navigateUp()
                        }
                    }
                }
            }

        }
        return binding.root
    }

}