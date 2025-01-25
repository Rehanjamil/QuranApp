package com.quran.rabiulqaloob

import androidx.fragment.app.Fragment
import com.quran.rabiulqaloob.utils.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BaseFragment: Fragment() {

    @Inject
    lateinit var preferenceHelper: PreferenceHelper
}