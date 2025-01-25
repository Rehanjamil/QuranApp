package com.quran.rabiulqaloob.generics

import com.google.android.material.progressindicator.CircularProgressIndicator

interface OnProgressChange {
    fun onChange(
        circularProgressIndicator: CircularProgressIndicator
    )
}