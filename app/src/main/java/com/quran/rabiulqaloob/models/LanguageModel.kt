package com.quran.rabiulqaloob.models

import com.quran.rabiulqaloob.generics.ListItemViewModel

data class LanguageModel(
    val name: String,
    val shortCode: String
): ListItemViewModel()