package com.quran.rabiulqaloob.generics.binding

import android.content.res.AssetManager
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import java.io.IOException


@BindingAdapter(
    value = ["fileName", "childFile", "parentPage"],
    requireAll = false
)
fun ImageView.loadFile(fileName: String, childFile: Boolean = false, parentPage: Int = 0) {
    val assetManager: AssetManager = context.assets
    try {
        val inputStream =
            if (childFile) {
                assetManager.open("images/page$parentPage/$fileName.png")
            } else {
                assetManager.open("images/page" + fileName + "_optimized.png")
            }
        val drawable = Drawable.createFromStream(inputStream, null)
        this.setImageDrawable(drawable)
    } catch (e: IOException) {
        // Handle the exception (e.g., log it or show an error message)
        e.printStackTrace()
    }
}

@BindingAdapter(
    value = ["value", "direction"],
    requireAll = false
)
fun View.setVisibility(value: String, direction: Boolean) {
    this.isVisible = (value.toInt() and 1) == (if (direction) 0 else 1)
}