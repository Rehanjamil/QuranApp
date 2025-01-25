package com.quran.rabiulqaloob.ui.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.WindowManager
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.quran.rabiulqaloob.BaseDialog
import com.quran.rabiulqaloob.R
import com.quran.rabiulqaloob.databinding.DownloadingDialogBinding
import com.quran.rabiulqaloob.generics.InfiniteViewPagerAdapter
import com.quran.rabiulqaloob.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DownloadingDialog : BaseDialog() {

    private lateinit var binding: DownloadingDialogBinding
    private val list = arrayListOf(
        "The Prophet (peace and blessings of Allaah be upon him) said, \"The best of you is the one who learns and teaches the Qur'aan\". (Sahih Bukhari - 5027)",
        "It is narrated on the authority of Abdullah ibn Masud that the Messenger of Allah, may Allah bless him and grant him peace, said: \"Whoever reads a letter from the Book of Allah, for him there is a good deed in return and this one good deed is equal to ten good deeds. I am not saying that alam is a letter but a is a letter, lam is a letter and mem is a letter.\" (Sunan al-Tirmidhi, 2910)",
        "Ā’ishah (may Allah be pleased with her) reported: The Messenger of Allah (may Allah’s peace and blessings be upon him) said: \"The one who recites the Qur'an skillfully will be in the company of the noble and righteous messenger-angels and the one who reads the Qur'an, but stutters and finds it difficult, receives a double reward.\"  (Sahih Muslim, 789)",
        "A group of people do not gather in a house amongst the Houses of Allah reciting the Book of Allah and studying it between themselves – except that tranquillity descends upon them, they are enveloped by mercy and surrounded by the angels – and Allah mentions them with those with Him (i.e. the higher angels). (Sahih Muslim, 2699)",
        "Wathila ibn al-Asqa’ (ra) reported that the Messenger of Allah ﷺ said; \"The Suhuf (scrolls) of Ibrahim were revealed on the first night of Ramadhan, the Torah on the sixth of Ramadhan,  The Injeel (Gospel) on the thirteenth of Ramadhan, and the Furqan (Qur’ān) on the twenty-fourth of Ramadhan.\"(Musnad Ahmad ,16984)",
        "Anas bin Malik (ra) reported that the Messenger of Allah ﷺ said; \"Allah has His own people among mankind.” They said, “O Messenger of Allah, who are they?” He said, “They are the people of the Qur’ān, the people of Allah and those who are closest to Him.\"(Ibn Majah , 215)",
        "The father of Abdullah ibn Burayda al-Aslami (ra) reported that the Messenger of Allah ﷺ said; “Whoever recited the Qur’ān and acted according to it; on the Day of Judgment his parents will be given to wear a crown whose light is better than the light of the sun in the dwellings of this world if it were among you. So what do you think of him who acts according to this?”(Musnad Ahmad, 15645)"
    )
    private lateinit var dots: Array<TextView?>
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity())
        binding = DownloadingDialogBinding.bind(
            layoutInflater.inflate(R.layout.downloading_dialog, null, false)
        )

        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        binding.rvHadith.adapter = InfiniteViewPagerAdapter(
            list, true, requireActivity()
        )
        binding.rvHadith.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                addBottomDots(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })
        binding.pbDownloadingProgress.progress
//        binding.rvHadith.bindViewPager(
//            ,
//            R.layout.hadith_item_layout,
//            infinite = true
//        )
//        autoScrollHelper = AutoScrollHelper(binding.rvHadith)
        binding.rvHadith.offscreenPageLimit = list.size - 1

        binding.rvHadith.clipToPadding = false
        binding.rvHadith.clipChildren = false
        lifecycleScope.launch {
            while (binding.pbDownloadingProgress.progress < 100) {
                delay(500)
                binding.pbDownloadingProgress.progress += 1
                binding.tvDownloadingProgress.text = "${binding.pbDownloadingProgress.progress}%"
                if (binding.pbDownloadingProgress.progress == 100) {
                    delay(200)
                    preferenceHelper.setBol(Constants.RESOURCE_DOWNLOADED, true)
                    findNavController().navigate(R.id.action_downloadingDialog_to_homeFragment)
                }
            }
        }
        addBottomDots(0)
        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        binding.rvHadith.pauseAutoScroll()
    }

    private fun addBottomDots(currentPage: Int) {
        dots = arrayOfNulls(list.size)
        val colorsActive = resources.getColor(R.color.white)
        val colorsInactive = resources.getColor(R.color.dot_dark_screen1)
        binding.layoutDots.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(requireActivity())
            dots[i]?.text = Html.fromHtml("&#8226;")
            dots[i]?.textSize = 50f
            dots[i]?.setTextColor(colorsInactive)
            binding.layoutDots.addView(dots[i])
        }
        if (dots.isNotEmpty()) {
            dots[currentPage % list.size]?.setTextColor(colorsActive)
        }
    }
}