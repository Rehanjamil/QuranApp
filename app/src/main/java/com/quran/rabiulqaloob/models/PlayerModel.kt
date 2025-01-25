package com.quran.rabiulqaloob.models

data class PlayerModel(
    val currentSurah: String = "0",
    val surahProgress: Int = 0,
    val isPlayerShown: Boolean = false,
    val isPlayedByUser:Boolean = true
)