package com.quran.rabiulqaloob.generics.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter(
    value = ["resourceId", "text"],
    requireAll = false
)
fun TextView.concatenateString(resourceId: Int, text: String) {
    this.text = this.context.getString(resourceId, text)
}