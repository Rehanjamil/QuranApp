package com.quran.rabiulqaloob.models

import android.view.View
import androidx.databinding.Bindable
import com.quran.rabiulqaloob.BR
import com.quran.rabiulqaloob.generics.ListItemViewModel

data class Juzz(
    val juzzName: String,
    val juzzArabic: String,
    val pageNo: String,
    val juzzIndex: String
) : ListItemViewModel() {

    @get:Bindable
    var isBookMarkType: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.listItemViewModel)
            notifyPropertyChanged(BR.bookMarkType)
        }

    @get:Bindable
    var isBookMarked: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.listItemViewModel)
            notifyPropertyChanged(BR.bookMarked)
        }

    fun changeBookMarkState(view: View){
        onItemClickListener?.onClick(view, adapterPosition, this)
        isBookMarked = isBookMarked.not()
    }
}