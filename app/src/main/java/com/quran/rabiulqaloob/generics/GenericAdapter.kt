package com.quran.rabiulqaloob.generics

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.quran.rabiulqaloob.BR
import com.quran.rabiulqaloob.R
import java.lang.reflect.Field
import java.util.*
import kotlin.properties.Delegates

/**
 * @param <T> generic data object with Data Binding
 * @author Ahsan Islam
 * @version 2.0
 * @apiNote Base adapter with generic listing & filteration & databinding adapter support
 * @apiNote To use data binding must use layout parent in xml files of views & data objects to populate data
 * @since 14-OCT-2022
 * @ref https://medium.com/android-news/using-databinding-like-a-pro-to-write-generic-recyclerview-adapter-f94cb39b65c4
 * @ref Upgrade MVVM https://medium.com/huawei-developers/how-to-use-recyclerview-with-databinding-mvvm-211f6b69a81a
</T> */


class GenericAdapter<T : ListItemViewModel, VH : RecyclerView.ViewHolder>(@LayoutRes val layoutId: Int) :
    PagingDataAdapter<T, VH>(object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {

            return false
        }
    }), Filterable {
    enum class ViewType {
        VERTICAL, HORIZONTAL, HORIZONTAL_MULTIPLE
    }

    enum class SortOrder {
        ASCENDING, DESCENDING, ALPHABETIC, NEWLY_ADDED
    }

    public var filteredList = arrayListOf<T>()
    var originalList = arrayListOf<T>()
    protected var context: Context? = null
    protected var inflater: LayoutInflater? = null
    protected var mFilter: Filter? = null
    protected var viewId = 0
    private var IS_EMPTY_LIST = false
    private var IS_LOADING = true
    private var IS_SWIPE_ENABLED = false
    private var isInfinite = false
    protected var originalView = 0
    protected var orientation = ViewType.VERTICAL
    protected var EmptyDrawable: Int? = null
    protected var EmptyLabel = "No Item Found"
    protected var EmptyLabelSpanned: SpannableString? = null
    protected var queryArgs: HashMap<String?, Int?> = HashMap<String?, Int?>()

    private var clickListener: OnItemClickListener? = null
    private var clickListenerChild: OnItemClickListener? = null
    private var pageChangeCallback: ViewPager2.OnPageChangeCallback? = null

    fun setOnClickListener(clickListener: OnItemClickListener?) {
        this.clickListener = clickListener
    }


    fun setOnChildClickListener(clickListener: OnItemClickListener?) {
        this.clickListenerChild = clickListener
    }

    fun isInfinite(
        bool: Boolean
    ){
        isInfinite = bool
    }

    fun setOnPageChangeCallback(pageChangeCallback: ViewPager2.OnPageChangeCallback?) {
        this.pageChangeCallback = pageChangeCallback
    }

    var isQueryParamSet = false
        get() = field
        private set(value) {
            field = value
        }

    var sizeChange: Int by Delegates.observable(0) { property, oldValue, newValue ->
        if (IS_SWIPE_ENABLED) {
            if (newValue > 0) {
                enableSwipeToDeleteAndUndo()
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    detachSwipe()
                }, 500)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater = inflater ?: LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)


        return (if (IS_EMPTY_LIST && !IS_LOADING) EmptyViewHolder(binding.root)
        else if (IS_LOADING)
            LoadingViewHolder(binding.root)
        else {
            val vh = GenericViewHolder<T>(binding)
//            binding.lifecycleOwner = vh
            vh
        }) as VH
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (IS_EMPTY_LIST && !IS_LOADING) {
        } else {
            if (!IS_LOADING) {
                val dataPosition = if (isInfinite)
                    position % filteredList.size
                else
                    position
                val itemViewModel = filteredList[dataPosition]
                itemViewModel.adapterPosition = dataPosition
                itemViewModel.isFirstChild = dataPosition == 0
                itemViewModel.isLastChild = dataPosition == filteredList.size - 1
                clickListener?.let { itemViewModel.onItemClickListener = it }
                clickListenerChild?.let { itemViewModel.onItemChildClickListener = it }
                pageChangeCallback?.let { itemViewModel.pageChangeCallback = it }
                (holder as GenericViewHolder<T>).bind(itemViewModel)
            }
        }
    }

//    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
//        super.onViewAttachedToWindow(holder)
//        if (holder is GenericAdapter<*>.GenericViewHolder<*>) {
//            holder.markAttach()
//        }
//    }
//
//    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
//        super.onViewDetachedFromWindow(holder)
//
//        if (holder is GenericAdapter<*>.GenericViewHolder<*>) {
//            holder.markDetach()
//        }
//    }

    //==============View Holders =====================================
    internal inner class GenericViewHolder<T : ListItemViewModel>(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
//        , LifecycleOwner

//        private val lifecycleRegistry = LifecycleRegistry(this)
//
//        init {
//            lifecycleRegistry.markState(Lifecycle.State.INITIALIZED)
//        }

        fun bind(itemViewModel: T) {
            binding.setVariable(BR.listItemViewModel, itemViewModel)
            // optional observer to update name if it changes (2-way binding through EditText)

            binding.executePendingBindings()
        }

//        fun markAttach() {
        // Lifecycle.State.CREATED doesn't work for this case
//             lifecycleRegistry.markState(Lifecycle.State.CREATED)
//            lifecycleRegistry.markState(Lifecycle.State.STARTED)
        // lifecycleRegistry.markState(Lifecycle.State.RESUMED)
//        }
//
//        fun markDetach() {
//            lifecycleRegistry.markState(Lifecycle.State.DESTROYED)
//        }
//
//        override fun getLifecycle(): Lifecycle {
//            return lifecycleRegistry
//        }

    }

    internal inner class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView
        var txt: TextView

        init {
            img = itemView.findViewById(R.id.imgEmpty)
            txt = itemView.findViewById(R.id.txtEmpty)
            EmptyDrawable?.let {
                img.setImageDrawable(ContextCompat.getDrawable(itemView.context, it))
            }
            if (EmptyLabelSpanned == null)
                txt.text = EmptyLabel
            else
                txt.text = EmptyLabelSpanned
        }
    }

    internal inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var shimmerFrameLayout: ShimmerFrameLayout? = null
//
//        init {
//            if (orientation == ViewType.VERTICAL) {
//                shimmerFrameLayout = itemView.findViewById(R.id.verticalLoader)
//            } else if (orientation == ViewType.HORIZONTAL) {
//                shimmerFrameLayout = itemView.findViewById(R.id.horizontalSingleLoader)
//            } else if (orientation == ViewType.HORIZONTAL_MULTIPLE) {
//                shimmerFrameLayout = itemView.findViewById(R.id.horizontalMultiLoader)
//            }
//            shimmerFrameLayout!!.visibility = View.VISIBLE
//            shimmerFrameLayout!!.startShimmer()
//        }
    }

    interface Binder<T> {
        fun bind(data: T)
    }


    interface OnItemClickListener {
        fun onClick(view: View, position: Int, data: ListItemViewModel)

        fun onLongClick(view: View, position: Int, data: ListItemViewModel) = 0
    }


    override fun getItemViewType(position: Int): Int {
        if (IS_EMPTY_LIST && !IS_LOADING) {
            return R.layout.empty_view
        } else if (IS_LOADING) {
            originalView = layoutId
            return R.layout.loading_view
        }
        return layoutId
    }


    override fun getItemCount(): Int {
        IS_EMPTY_LIST = filteredList.isEmpty() /*&& originalList.isEmpty()*/
        return if (IS_EMPTY_LIST && !IS_LOADING) {
            sizeChange = 0
            1
        } /*when loading is finish and result is empty*/
        else if (IS_LOADING && orientation == ViewType.HORIZONTAL_MULTIPLE) {
            sizeChange = 0
            1
        } /*when view is loading and view has multiple subview in a row*/
        else if (IS_LOADING) {
            sizeChange = 0
            1
        } /*when view is signle row*/
        else {
            sizeChange = if(isInfinite) Int.MAX_VALUE else filteredList.size
            sizeChange
        } /*when api finish loading and has some items*/
    }

    override fun getFilter(): Filter? {
        return if (mFilter != null) mFilter else null
    }

    /**
     * This Method is Used to add single Data of Type T,
     * @param at used to add Item at Specified Position
     * @param t data of T
     */
    @Synchronized
    fun add(at: Int, t: T) {
        IS_LOADING = false
        val filtersize = filteredList.size
        originalList.add(at, t)
        if (filteredList.size == filtersize)
            filteredList.add(at, t)
        notifyItemInserted(at)
    }

    @Synchronized
    fun add(t: T, notify: Boolean = true) {
        IS_LOADING = false
        originalList.add(t)
        val position = originalList.indexOf(t)
        if (notify)
            notifyItemInserted(position)
    }

    /**
     * This Method is Used to add List of Data of Type T,
     * Added Notify Bit Default True to notify adapter
     * @param list1 data T List
     * @param notify used to Notify Adapter- By Default its true*/
    @Synchronized
    fun addAll(list1: List<T>, notify: Boolean = true) {
        IS_LOADING = false
        originalList = list1 as ArrayList<T>
        filteredList = list1
        if (notify)
            notifyDataSetChanged()
    }

    /**
     * This Method is Used to add List of Data of Type T in Previous list of Adapter,
     * Added Notify Bit Default True to notify adapter
     * @param list1 data T List
     * @param notify used to Notify Adapter- By Default its true*/
    @Synchronized
    fun appendAll(list1: List<T>, notify: Boolean = true) {
        IS_LOADING = false
        originalList.addAll(list1)
        filteredList = originalList
        if (notify)
            notifyDataSetChanged()
    }

    @Synchronized
    fun addToFirst(t: T) {
        originalList.add(0, t)
        val position = originalList.indexOf(t)
        notifyItemInserted(position)
    }

    @Synchronized
    fun clear() {
        originalList.clear()
        filteredList.clear()
        notifyDataSetChanged()
    }

    val isEmpty: Boolean
        get() = itemCount == 0

    fun reverse() {
        Collections.reverse(originalList)
        Collections.reverse(filteredList)
    }

    @Synchronized
    fun removePosition(position: Int) {
        originalList.removeAt(position)
        notifyItemRemoved(position)
    }

    @Synchronized
    fun removeFilteredPosition(position: Int) {
        val item = filteredList.get(position)
        originalList.remove(item)
        filteredList.remove(item)
        notifyItemRemoved(position)
    }

    @Synchronized
    fun removeItem(item: T, position: Int) {
        val filteredSize = filteredList.size
        originalList.remove(item)
        if (filteredList.size == filteredSize)
            filteredList.remove(item)
        notifyItemRemoved(position)

    }

    @Synchronized
    fun setPosition(t: T, position: Int) {
        var position = position
        originalList[position] = t
        position = originalList.indexOf(t)
        notifyItemChanged(position)
    }

    @Synchronized
    fun addPosition(t: T, position: Int) {
        var position = position
        originalList.add(position, t)
        position = originalList.indexOf(t)
        notifyItemInserted(position)
    }

    @Synchronized
    fun removerPosition(position: Int) {
        val t = originalList[position]
        originalList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, originalList.size)
    }

    @Synchronized
    fun updatePosition(t: T, position: Int) {
        originalList[position] = t
        notifyItemChanged(position)
        notifyItemRangeChanged(position, originalList.size)
    }

    val data: MutableList<T>
        get() = filteredList

    val originalSize: Int
        get() = originalList.size

    fun setInitNewList(list: List<T>?) {
        originalList = ArrayList(list)
        notifyDataSetChanged()
    }

    fun setExistingList(list: ArrayList<T>) {
        originalList = list
        notifyDataSetChanged()
    }

    /**
     * @param field used for filter data base on value of this Key
     * @param value used to match the value for filtering list
     * @apiNote this method is applied on FilteredList and original list remain un-touched,
     * @apiNote and when value is empty original list is assigned to filteredList
     * @since 05-04-2021
     */
    @Synchronized
    fun filterBy(
        field: String,
        value: String,
        vararg agr: String,
        shouldReplaceSpace: Boolean = true,
        Callback: (() -> Unit)? = null
    ) {
        Handler(Looper.getMainLooper()).post {
            val ignoreString = if (agr.size >= 1) agr[0] else ""
            val filterList = ArrayList<T>()
            var selectedIndex = -1
            if (originalList.isEmpty()) {
            } else if (value.isEmpty()) {
                filteredList = originalList
                notifyDataSetChanged()
            } else {
                val item: Any? = originalList[0]
                val fields = item!!.javaClass.declaredFields
                var hasField = false
                for (i in fields.indices) {
                    if (field == fields[i].name) {
                        selectedIndex = i
                        hasField = true
                        break
                    }
                }
                if (hasField) {
                    for (Item in originalList) {
                        val myfield: Array<Field> = Item!!::class.java.getDeclaredFields()
                        try {
                            myfield[selectedIndex].isAccessible = true
                            val finalValue = if (shouldReplaceSpace)
                                value.lowercase().replace(" ", "_")
                            else
                                value.lowercase()
                            if (myfield[selectedIndex][Item] != null
                                && (myfield[selectedIndex][Item].toString().lowercase()
                                    .equals(finalValue)
                                        || ignoreString != "" && myfield[selectedIndex][Item].toString()
                                    .lowercase().contains(ignoreString.lowercase()))
                            )
                                filterList.add(Item)
                        } catch (e: IllegalAccessException) {
                            e.printStackTrace()
                        }
                    }
                    filteredList = filterList
                    notifyDataSetChanged()

                }
            }
            Callback?.invoke()
        }
    }


    /**
     * @param arg is the multi params key pairs field names and relevant values
     * @apiNote this method is applied on FilteredList and original list remain un-touched,
     * @apiNote and when value is empty original list is assigned to filteredList
     * @since 28-04-2021
     */
    fun filterByMulti(vararg arg: String) {
        if (originalList.isEmpty() && filteredList.isEmpty())
            return
        val keyPair = HashMap<String, String>()
        for (pair in arg.indices step 2) {
            if (pair + 1 < arg.size) {
                val fieldName = arg[pair]
                val fieldValue = arg[pair + 1]
                keyPair.put(fieldName, fieldValue)
            }
        }
        if (!keyPair.isNullOrEmpty()) {
            Handler(Looper.getMainLooper()).post {
                val filterList = ArrayList<T>(originalList)
                val item: Any? = originalList[0]
                val fields = item!!.javaClass.declaredFields
                for (i in fields.indices) {
                    if (keyPair.containsKey(fields[i].name)) {
                        val fieldToCheck = fields[i].name
                        val itemToRemove = ArrayList<T>()
                        for (k in 0 until filterList.size) {
                            val myfield: Array<Field> = filterList[k]!!::class.java.declaredFields
                            try {
                                myfield[i].isAccessible = true
                                if (myfield[i][filterList[k]] != null) {
                                    val fieldVal =
                                        myfield[i][filterList[k]].toString().lowercase()
                                    val fieldValToCheck =
                                        keyPair.getValue(fields[i].name).lowercase()
                                    if (fieldValToCheck == "isEmpty") {
                                        if (fieldVal.isNotEmpty()) {
                                            itemToRemove.add(filterList[k])
                                        } else
                                            Log.i(TAG, "filterByMulti: yes")
                                    } else {
                                        if (!fieldVal.equals(fieldValToCheck.replace(" ", "_"))) {
                                            itemToRemove.add(filterList[k])
                                        } else
                                            Log.i(TAG, "filterByMulti: yes")
                                    }

                                }


                            } catch (e: IllegalAccessException) {
                                e.printStackTrace()
                            }
                        }
                        filterList.removeAll(itemToRemove)
                        itemToRemove.clear()
                    }
                }

                filteredList = filterList
                notifyDataSetChanged()
            }
        } else {
            filteredList = originalList
            notifyDataSetChanged()
        }
    }

    /**
     * @param value used to match the value for filtering list with all its fields match any field it will return
     * @apiNote this method is applied on FilteredList and original list remain un-touched,
     * @apiNote and when value is empty original list is assigned to filteredList
     * @since 28-04-2021
     */
    fun searchBy(value: String, vararg arg: String) {
        val fieldName = if (arg.size > 1) arg[0] else ""
        val fieldValue = if (arg.size > 1) arg[1] else ""
        val filterList: ArrayList<T> = ArrayList<T>()

        filterBy(fieldName, fieldValue) {
            Log.d("TAG", "onQueryTextChange: filterBy=")
            Log.d("TAG", "onQueryTextChange: ${filteredList.size}")
            if (filteredList.isNotEmpty()) {

                val item: Any? = originalList[0]
                val fields = item!!.javaClass.declaredFields
                for (Item in filteredList) {
                    Log.d("TAG", "onQueryTextChange: queryArgs= $queryArgs")
                    for ((_, value1) in queryArgs) {
                        try {
                            fields[value1!!].isAccessible = true
                            var compare = false
                            if (fields[value1][Item] is Boolean) {
                                compare =
                                    fields[value1][Item] == java.lang.Boolean.parseBoolean(value)
                            } else if (fields[value1][Item] is String) {
                                compare = (fields[value1][Item] as String).lowercase()
                                    .contains(value.lowercase())
                            } else if (fields[value1][Item] is Int) {
                                compare = fields[value1][Item] == value.toInt()
                            } else if (fields[value1][Item] is Double) {
                                compare = fields[value1][Item] == value.toDouble()
                            } else if (fields[value1][Item] is Long) {
                                compare = fields[value1][Item] == value.toLong()
                            } else if (fields[value1][Item] is Float) {
                                compare = fields[value1][Item] == value.toFloat()
                            }
                            Log.d("TAG", "onQueryTextChange: compare= $compare")
                            if (compare) {
                                filterList.add(Item)
                                break
                            }
                        } catch (e: Exception) {
                            Log.d("TAG", "onQueryTextChange: ${e.message}")
                            e.printStackTrace()
                        }
                    }
                }
            }
            filteredList = filterList
            notifyDataSetChanged()
        }

    }

    fun sortBy(field: String, value: String, sortField: String, sortOrder: SortOrder) {
        if (originalList.isEmpty())
            return
        filterBy(field, value) {
            val item: Any? = originalList[0]
            val fields = item!!.javaClass.declaredFields
            var hasField = false
            var selectedIndex = -1
            for (i in fields.indices) {
                if (sortField == fields[i].name) {
                    selectedIndex = i
                    hasField = true
                    break
                }
            }
            when (sortOrder) {
                SortOrder.ALPHABETIC -> {
                    if (hasField) {
                        filteredList.sortWith(Comparator { o1, o2 ->
                            val myfield1: Array<Field> = o1!!::class.java.getDeclaredFields()
                            val myfield2: Array<Field> = o2!!::class.java.getDeclaredFields()
                            myfield1[selectedIndex].isAccessible = true
                            myfield2[selectedIndex].isAccessible = true

                            myfield1[selectedIndex].get(o1).toString().compareTo(
                                myfield2[selectedIndex].get(o2).toString(),
                                ignoreCase = true
                            )
                        })
                    }

                }

                SortOrder.NEWLY_ADDED -> {
                    if (hasField) {
                        filteredList.sortWith(Comparator { o1, o2 ->
                            val myfield1: Array<Field> = o1!!::class.java.getDeclaredFields()
                            val myfield2: Array<Field> = o2!!::class.java.getDeclaredFields()
                            myfield1[selectedIndex].isAccessible = true
                            myfield2[selectedIndex].isAccessible = true

                            myfield2[selectedIndex].get(o2).toString().compareTo(
                                myfield1[selectedIndex].get(o1).toString(),
                                ignoreCase = true
                            )
                        })
                    }
                }

                else -> {}
            }
            notifyDataSetChanged()
        }
    }


    /**
     * @param field     used for filter data base on value of this Key
     * @param sortOrder used to sort the list acs or desc
     * @apiNote this method is applied on Filtered List and original list remain un-touched,
     * @apiNote and when value is empty original list is assigned to filteredList
     * @since 28-04-2021
     */
    var defaultOrder = SortOrder.ASCENDING

    @Synchronized
    fun sortBy(field: String, sortOrder: SortOrder) {
        if (originalList.isEmpty()) return
        val item: Any? = originalList[0]
        val fields = item!!.javaClass.declaredFields
        var field1: Field? = null
        var hasField = false
        for (value in fields) {
            if (field == value.name) {
                hasField = true
                field1 = value
                break
            }
        }
        if (hasField) {
            val finalField = field1
            val myComparator = Comparator<T?> { obj1, obj2 ->
                try {
                    finalField!!.isAccessible = true
                    var compare = 0
                    if (finalField[obj1] is Boolean) {
                        compare =
                            (finalField[obj1] as Boolean).compareTo((finalField[obj2] as Boolean))
                    } else if (finalField[obj1] is String) {
                        compare =
                            (finalField[obj1] as String).compareTo((finalField[obj2] as String))
                    } else if (finalField[obj1] is Int) {
                        compare = (finalField[obj1] as Int).compareTo((finalField[obj2] as Int))
                    } else if (finalField[obj1] is Double) {
                        compare =
                            (finalField[obj1] as Double).compareTo((finalField[obj2] as Double))
                    } else if (finalField[obj1] is Long) {
                        compare =
                            (finalField[obj1] as Long).compareTo((finalField[obj2] as Long))
                    } else if (finalField[obj1] is Float) {
                        compare =
                            (finalField[obj1] as Float).compareTo((finalField[obj2] as Float))
                    }
                    return@Comparator if (defaultOrder == SortOrder.ASCENDING) {
                        compare
                    } else {
                        compare * -1
                    }
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                    return@Comparator 0
                }
            }
            Collections.sort(originalList, myComparator)
            filteredList = originalList
            defaultOrder = if (defaultOrder == SortOrder.ASCENDING)
                SortOrder.DESCENDING
            else
                SortOrder.ASCENDING
            notifyDataSetChanged()
        }
    }

    @Synchronized
    fun sortBy(sortOrder: SortOrder?) {
        if (originalList.isEmpty()) return
        Collections.reverse(originalList)
        filteredList = originalList
        notifyDataSetChanged()
    }

    operator fun get(position: Int): T? {
        return filteredList[position]
    }

    var filterSelectedItem = -1
    fun getFilters(query: String?, field: String, type: String?) {
        if (filterSelectedItem == -1) {
            val item: Any? = originalList[0]
            val fields = item!!.javaClass.declaredFields
            for (i in fields.indices) {
                if (field == fields[i].name) {
                    filterSelectedItem = i
                    Log.d(TAG, "getFilters: Found")
                    break
                }
            }
        }
        if (filter == null) {
            Log.d(TAG, "getFilters: null,creating")
            mFilter = object : Filter() {
                override fun performFiltering(constraint: CharSequence): FilterResults {
                    val resultsData: MutableList<T?> = ArrayList()
                    val filter = FilterResults()
                    if (!constraint.toString().isEmpty()) {
                        Log.d(TAG, "performFiltering: not expty")
                        if (filterSelectedItem != -1) {
                            for (Item in originalList) {
                                val myfield: Array<Field> =
                                    Item!!::class.java.getDeclaredFields()
                                try {
                                    myfield[filterSelectedItem].isAccessible = true
                                    if (myfield[filterSelectedItem][Item] != null &&
                                        myfield[filterSelectedItem][Item].toString()
                                            .lowercase()
                                            .contains(query!!)
                                    ) {
                                        resultsData.add(Item)
                                        Log.d(TAG, "performFiltering: found Data")
                                    }
                                } catch (e: IllegalAccessException) {
                                    e.printStackTrace()
                                }
                            }
                            filter.values = resultsData
                            filter.count = resultsData.size
                        }
                    } else {
                        filter.values = originalList
                        filter.count = originalList.size
                    }
                    return filter
                }

                override fun publishResults(constraint: CharSequence, results: FilterResults) {
                    if (results.count != 0) filteredList =
                        results.values as ArrayList<T> else filteredList.clear()
                    notifyDataSetChanged()
                }
            }
            filter?.filter(query)
        } else filter?.filter(query)
    }

    @Synchronized
    fun setLoaderOrientation(orientatio: ViewType) {
        orientation = orientatio
    }

    @Synchronized
    fun setEmptyView(Drawable: Int? = null, label: String?, spannedLabel: SpannableString? = null) {
        EmptyDrawable = Drawable
        if (label != null)
            EmptyLabel = label
        EmptyLabelSpanned = spannedLabel
    }

    @Synchronized
    fun setEmptyViewText(label: String?, spannedLabel: SpannableString? = null) {
        if (label != null)
            EmptyLabel = label
        EmptyLabelSpanned = spannedLabel
    }

    @Synchronized
    fun setEmptyViewLogo(Drawable: Int? = null) {
        EmptyDrawable = Drawable
    }

    @Synchronized
    fun addQueryArgs(vararg field: String): Boolean {
        if (!isQueryParamSet)
            queryArgs.clear()

        if (originalList.isNotEmpty()) {
            val item: Any? = originalList[0]
            val fields = item!!.javaClass.declaredFields
            field.forEach {
                for (i in fields.indices) {
                    if (field.contains(fields[i].name)) {
                        queryArgs[it] = i
                        break
                    }
                }
            }

            isQueryParamSet = true
        }
        return isQueryParamSet
    }

    companion object {
        const val TAG = "BaseAdapter"
    }

    /*Enabled Swipe to Delete Function*/

    //    var swipeToDeleteCallback: SwipeToDeleteCallback? = null
    var itemTouchhelper: ItemTouchHelper? = null
    var isAttached = false
    var swipeRecyclerView: RecyclerView? = null
    var swipeCallback: ((T, Int, Callback: (Boolean) -> Unit) -> Unit)? = null
    fun enableSwipeToDeleteAndUndo(
        recyclerView: RecyclerView? = null,
        Callback: ((T, Int, Callback: (isNotDeleted: Boolean) -> Unit) -> Unit)? = null
    ) {
//        if (swipeToDeleteCallback == null) {
//            IS_SWIPE_ENABLED = true
//            swipeCallback = Callback
//            swipeToDeleteCallback = object :
//                SwipeToDeleteCallback(recyclerView!!.context) {
//                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                    val position = viewHolder.bindingAdapterPosition
//                    val item = data.get(position)
//                    swipeCallback?.invoke(item, position) { isNotDeleted ->
//                        if (isNotDeleted) {
//                            removeItem(item, position)
//                            add(position, item!!)
//                        }
//                    }
//                }
//            }
        swipeRecyclerView = recyclerView
//        }
        if (itemTouchhelper == null)
//            itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback!!)
            if (!isAttached) {
                isAttached = true
                Handler(Looper.getMainLooper()).postDelayed({
                    itemTouchhelper?.attachToRecyclerView(swipeRecyclerView)
                }, 500)
            }
    }

    fun detachSwipe() {
        if (itemTouchhelper != null) {
            isAttached = false
            itemTouchhelper?.attachToRecyclerView(null)
        }
    }


    fun isEmptyView(): Boolean {
        return IS_EMPTY_LIST
    }

    /*End of Swipe to Delete Function*/
}