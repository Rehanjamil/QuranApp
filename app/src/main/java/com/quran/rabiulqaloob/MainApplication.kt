package com.quran.rabiulqaloob

import android.app.Application
import android.content.Context
import com.akexorcist.localizationactivity.ui.LocalizationApplication
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale

@HiltAndroidApp
class MainApplication : LocalizationApplication() {

    override fun getDefaultLanguage(context: Context): Locale = Locale.getDefault()
}