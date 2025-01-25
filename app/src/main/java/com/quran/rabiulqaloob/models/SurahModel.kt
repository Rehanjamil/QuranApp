package com.quran.rabiulqaloob.models

import android.view.View
import androidx.databinding.Bindable
import com.quran.rabiulqaloob.BR
import com.quran.rabiulqaloob.generics.ListItemViewModel

data class SurahModel(
    val number: Int,
    val name: String,
    val englishName: String,
    val englishNameTranslation: String,
    val numberOfAyahs: Int,
    val revelationType: String,
    val pageNo: Int
): ListItemViewModel(){

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