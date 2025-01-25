package com.quran.rabiulqaloob.generics.binding

import android.graphics.Rect
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.quran.rabiulqaloob.generics.GenericAdapter
import com.quran.rabiulqaloob.generics.ListItemViewModel

@BindingAdapter(
    value = ["itemViewModels", "layout", "onItemClick", "onItemChildClick", "onPageChangeCallback", "emptyDrawable", "emptyTitle", "myLayoutManager", "mOrientation", "rowCount", "infinite"],
    requireAll = false
)
fun RecyclerView.bindItemViewModels(
    items: List<ListItemViewModel>?,
    layout: Int,
    clickListener: GenericAdapter.OnItemClickListener?,
    onItemChildClick: GenericAdapter.OnItemClickListener?,
    callback: ViewPager2.OnPageChangeCallback?,
    emptyDrawable: Int?,
    emptyTitle: String?,
    layoutManager: String? = "",
    orientation: String? = "vertical",
    rowCount: String? = "2",
    infinite: Boolean = false
) {
    val adapter = getOrCreateAdapter(this, layout)
    if (emptyDrawable != null)
        adapter.setEmptyViewLogo(emptyDrawable)

    if (emptyTitle != null)
        adapter.setEmptyViewText(emptyTitle)
    adapter.setOnClickListener(clickListener)
    adapter.setOnChildClickListener(onItemChildClick)
    adapter.isInfinite(infinite)
    callback?.let {
        adapter.setOnPageChangeCallback(callback)
    }


    if (items != null) {
        adapter.addAll(ArrayList(items))
    }
    if (layoutManager == "grid") {
        val staggeredGridLayoutManager =
            object : StaggeredGridLayoutManager(
                rowCount?.toInt() ?: 1,
                if (orientation == "vertical") LinearLayoutManager.VERTICAL else LinearLayoutManager.HORIZONTAL
            ) {
                override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                    lp.width = width / (rowCount?.toInt() ?: 1)
                    return true
                }
            }
        this.layoutManager = staggeredGridLayoutManager
    } else if (layoutManager == "linear") {
        val linearLayoutManager = LinearLayoutManager(
            context,
            if (orientation == "vertical") LinearLayoutManager.VERTICAL else LinearLayoutManager.HORIZONTAL,
            false
        )
        this.layoutManager = linearLayoutManager
    } else if (layoutManager == "center") {
//        val linearLayoutManager =CenterZoomLayout(context,10)
//        this.layoutManager = linearLayoutManager
    }
}

private fun getOrCreateAdapter(
    recyclerView: RecyclerView,
    layout: Int,
): GenericAdapter<ListItemViewModel, RecyclerView.ViewHolder> {
    return if (recyclerView.adapter != null && recyclerView.adapter is GenericAdapter<*, *>) {
        (recyclerView.adapter as GenericAdapter<ListItemViewModel, RecyclerView.ViewHolder>)
    } else {
        val rv = GenericAdapter<ListItemViewModel, RecyclerView.ViewHolder>(layout)
        recyclerView.adapter = rv
        rv
    }
}


@BindingAdapter("datalist")
fun RecyclerView.bindRecyclerView(list: List<ListItemViewModel>?) {
    val adapter: GenericAdapter<ListItemViewModel, RecyclerView.ViewHolder> =
        this.adapter as GenericAdapter<ListItemViewModel, RecyclerView.ViewHolder>
    if (list != null) {
        adapter.addAll(list)
    } else {
        adapter.addAll(ArrayList())
    }
}


@BindingAdapter("setGenericAdapter")
fun RecyclerView.setGenericAdapter(
    adapter: GenericAdapter<ListItemViewModel, RecyclerView.ViewHolder>?,
) {
    adapter?.let {
        this.adapter = it
    }
}


@BindingAdapter("manageState")
fun manageState(progressBar: ProgressBar, state: Boolean) {
    progressBar.visibility = if (state) View.VISIBLE else View.GONE
}

@BindingAdapter(
    value = ["overlapItemViewModels", "overlapLayout", "onItemClick", "overlapDecoration", "isDetailView"],
    requireAll = false
)
fun RecyclerView.bindOverlapItemViewModels(
    items: List<ListItemViewModel>?,
    layout: Int,
    clickListener: GenericAdapter.OnItemClickListener?,
    overlapDecoration: Boolean? = false,
    isDetailView: Boolean? = false,

    ) {

    val adapter = getOrCreateAdapter(this, layout)
    adapter.setEmptyViewText("")
    val lay = LinearLayoutManager(this.context)
    lay.orientation =
        if (isDetailView == true) LinearLayoutManager.VERTICAL else LinearLayoutManager.HORIZONTAL



    this.layoutManager = lay
    adapter.setOnClickListener(clickListener)
    if (items != null) {
        adapter.addAll(ArrayList(items))
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }


    if (isDetailView == true) {
        this.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    } else {
        this.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }
    if (overlapDecoration == true && isDetailView == false) {
        this.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State,
            ) {
                val itemPosition = parent.getChildLayoutPosition(view)

                Log.d("bindOverlapItemViewModels", "list items: ${items?.size}")
                Log.d("bindOverlapItemViewModels", "itemPosition: $itemPosition")


                if (itemPosition != 0)
                    outRect.set(-20, 0, 0, 0)

            }

        })
    }

}


@BindingAdapter(
    value = ["curvedViewModel", "curvedLayout", "curveHorizontalOffset"],
    requireAll = false
)
fun RecyclerView.bindCurvedItemWithRv(
    curvedViewModel: List<ListItemViewModel>?,
    curvedLayout: Int,
    curveHorizontalOffset: Int = 10,
) {
//    val lay = TurnLayoutManager(this.context, curveHorizontalOffset,)
//    this.layoutManager = lay
    this.adapter = getOrCreateAdapter(this, curvedLayout).apply {
        addAll(ArrayList(curvedViewModel ?: emptyList()))
    }

}

@BindingAdapter(
    value = ["advCurvedViewModel", "advCurvedLayout", "advCurveRadius", "advCurvePeak"],
    requireAll = false
)
fun RecyclerView.bindCurvedItemWithRv(
    advCurvedViewModel: List<ListItemViewModel>?,
    advCurvedLayout: Int,
    advCurveRadius: Int = 10,
    advCurvePeak: Int = 20,
) {
//    val lay = AdvanceTurnLayoutManager(this.context,AdvanceTurnLayoutManager.Gravity.END, AdvanceTurnLayoutManager.Orientation.HORIZONTAL,advCurveRadius,advCurvePeak,true)
//    val lay = CircleLayoutManager(this.context)
    //set the visable range of degrees
//    lay.apply {
//        setDegreeRangeWillShow(-120,130)
//        setScrollSpeed(5f)
//        radius=resources.getDimension(com.intuit.sdp.R.dimen._80sdp).roundToInt()
//        intervalAngle=30
//    }
//    this.layoutManager = lay
    this.adapter = getOrCreateAdapter(this, advCurvedLayout).apply {
        addAll(ArrayList(advCurvedViewModel ?: emptyList()))
    }

}

@BindingAdapter(
    value = ["viewPagerItems", "layout", "onPageChangeCallback", "onItemClick", "infinite"],
    requireAll = false
)
fun ViewPager2.bindViewPager(
    items: List<ListItemViewModel>?,
    layout: Int,
    callback: ViewPager2.OnPageChangeCallback? = null,
    clickListener: GenericAdapter.OnItemClickListener? = null,
    infinite: Boolean = false
) {

    val adapter = getOrCreateAdapter(this, layout)
    if (items != null) {
        adapter.addAll(ArrayList(items))
    } else {
        adapter.addAll(ArrayList())
    }
    adapter.isInfinite(infinite)
    adapter.setOnClickListener(clickListener)
    adapter.setOnPageChangeCallback(callback)
    this.adapter = adapter
    if(callback!= null) {
        this.registerOnPageChangeCallback(callback)
        adapter.setOnPageChangeCallback(callback)
    }
}


private fun getOrCreateAdapter(
    viewpager: ViewPager2,
    layout: Int,
): GenericAdapter<ListItemViewModel, RecyclerView.ViewHolder> {
    return if (viewpager.adapter != null && viewpager.adapter is GenericAdapter<*, *>) {
        (viewpager.adapter as GenericAdapter<ListItemViewModel, RecyclerView.ViewHolder>)
    } else {
        val rv = GenericAdapter<ListItemViewModel, RecyclerView.ViewHolder>(layout)
        viewpager.adapter = rv
        rv
    }
}

@BindingAdapter(
    value = ["size", "direction", "value"],
    requireAll = false
)
fun RecyclerView.setRoundRobinVisibility(
    size: Int,
    direction: Boolean,
    value: String
) {
    this.isVisible = size != 0 && (value.toInt() and 1) == (if (direction) 0 else 1)

}