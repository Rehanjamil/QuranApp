package com.quran.rabiulqaloob.models

import com.quran.rabiulqaloob.generics.ListItemViewModel

data class PageSubModel(
    val parentPage: Int,
    val itemNo: String,
): ListItemViewModel()