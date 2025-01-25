package com.quran.rabiulqaloob.generics

import android.text.SpannableString
import androidx.annotation.LayoutRes
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.viewpager2.widget.ViewPager2
import com.quran.rabiulqaloob.R
import com.quran.rabiulqaloob.BR

abstract class ListItemViewModel : BaseObservable() {
    @get:LayoutRes
    open val layoutId: Int = 0

    open val viewType: Int
        get() = GenericViewModelType.ACTUAL_DATA_VIEW.value

    @get:Bindable
    var viewVisibility: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.listItemViewModel)
            notifyPropertyChanged(BR.viewVisibility)
        }

    var adapterPosition: Int = -1

    var additionalParams = HashMap<String, Any>()

    fun getParamByKey(key: String, defaultVal: Any? = ""): Any {
        if (additionalParams.containsKey(key)) {
            return additionalParams[key] ?: defaultVal!!
        }
        return defaultVal!!
    }

    var onItemClickListener: GenericAdapter.OnItemClickListener? = null

    var onItemChildClickListener: GenericAdapter.OnItemClickListener? = null

    var pageChangeCallback: ViewPager2.OnPageChangeCallback? = null

    var isLastChild: Boolean = false
        get() =
            field


    var isFirstChild: Boolean = false

    @get:Bindable
    var isSelected: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.listItemViewModel)
            notifyPropertyChanged(BR.selected)
        }

    fun changeSelection() {
        isSelected = isSelected.not()
    }

    fun changeVisibility() {
        viewVisibility = viewVisibility.not()
    }

    fun changeVisibility(visibility: Boolean) {
        viewVisibility = visibility
    }

    fun dummy() {}
}

class EmptyViewModel(
    var emptyDrawable: Int? = R.drawable.bg_rounded_corners,
    var emptyLabel: String? = "No Item Found",
    var EmptyLabelSpanned: SpannableString? = null
) : ListItemViewModel() {
    override val layoutId: Int = R.layout.empty_view
    override val viewType: Int = GenericViewModelType.EMPTY_VIEW.value
}

class LoadingViewModel(var orientation: ViewType = ViewType.VERTICAL) : ListItemViewModel() {
    override val layoutId: Int = R.layout.loading_view
    override val viewType: Int = GenericViewModelType.LOADING_VIEW.value

    enum class ViewType {
        VERTICAL, HORIZONTAL, HORIZONTAL_MULTIPLE
    }
}

enum class GenericViewModelType(val value: Int) {
    EMPTY_VIEW(0), LOADING_VIEW(1), ACTUAL_DATA_VIEW(2)
}