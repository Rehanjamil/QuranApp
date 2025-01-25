package com.quran.rabiulqaloob.models

import com.quran.rabiulqaloob.generics.ListItemViewModel
import java.io.Serializable

data class PageModel(
    val pageNo: String,
    val juzzNo: String,
    val surahNames: String,
    val pageSubModel: ArrayList<PageSubModel> = arrayListOf()
): ListItemViewModel(), Serializable