package com.quran.rabiulqaloob.generics

import com.quran.rabiulqaloob.R
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.asksira.loopingviewpager.LoopingPagerAdapter
/**
 * Adapter class for car photos
 * */
class InfiniteViewPagerAdapter(
    private val photosList: ArrayList<String>,
    isInfinite: Boolean,
    var context: Activity,
) : LoopingPagerAdapter<String>(photosList, isInfinite) {


    override fun getItemViewType(listPosition: Int): Int {
        return listPosition
    }

    override fun bindView(
        convertView: View,
        listPosition: Int,
        viewType: Int
    ) {
        val textView = convertView.findViewById<TextView>(R.id.text)
        textView.text = photosList[listPosition]
    }

    override fun inflateView(
        viewType: Int,
        container: ViewGroup,
        listPosition: Int
    ): View {
        return LayoutInflater.from(container.context)
            .inflate(R.layout.hadith_item_layout, container, false)
    }

}
