package com.quran.rabiulqaloob.models

import com.quran.rabiulqaloob.generics.ListItemViewModel
import java.text.SimpleDateFormat
import java.util.Locale

data class BookmarkModel(
    val name: String,
    val englishName: String,
    val createdAt: Long = System.currentTimeMillis(),
    val pageNo: Int,
    val type: Int
) : ListItemViewModel() {

    fun getCreationDateTime(): String = SimpleDateFormat(
        "dd MMM, yyyy - hh:mm a", Locale.ENGLISH
    ).format(createdAt)
}