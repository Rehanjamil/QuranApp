package com.quran.rabiulqaloob

import androidx.fragment.app.DialogFragment
import com.quran.rabiulqaloob.utils.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BaseDialog: DialogFragment() {
    @Inject
    lateinit var preferenceHelper: PreferenceHelper

}