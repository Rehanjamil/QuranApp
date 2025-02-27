package com.quran.rabiulqaloob.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quran.rabiulqaloob.models.BookmarkModel
import com.quran.rabiulqaloob.models.SurahModel
import javax.inject.Inject


/**
 * SharedPreferences Helper class for writing in preferences and getting the data from the preferences
 * */
class PreferenceHelper @Inject constructor(
    val context: Context
) {
    //	private Context context_;
    private val sharedPreferences: SharedPreferences = context
        .getSharedPreferences(Constants.PREF_NAMES, Activity.MODE_PRIVATE)


    /**
     *  Functions for writing int in the sharedPreferences
     * */
    fun setIntOther(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    /**
     *  Functions for writing int in the sharedPreferences
     * */
    fun setInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    /**
     *  Functions for writing string in the sharedPreferences
     * */
    fun saveLanguage(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    /**
     *  Functions for writing string in the sharedPreferences
     * */
    fun setString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    /**
     *  Functions for writing string in the sharedPreferences
     * */
    fun setAppString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
        sharedPreferences.edit()
    }

    /**
     *  Functions for writing float in the sharedPreferences
     * */
    fun saveLoc(key: String, value: Float) {
        sharedPreferences.edit().putFloat(key, value).apply()
    }

    /**
     *  Functions for writing long in the sharedPreferences
     * */
    fun setLong(key: String, value: Long) {
        sharedPreferences.edit().putLong(key, value).apply()
    }

    /**
     *  Functions for writing bool in the sharedPreferences
     * */
    fun setBol(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    /**
     *  Functions for writing bool in the sharedPreferences
     * */
    fun setBolOther(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    //    /**
//     *  Functions for writing object in the sharedPreferences
//     * */
//    fun saveObject(model: Any, key: String) {
//        val gson = Gson()
//        val jsonFavorites = gson.toJson(model)
//        sharedPreferences.edit().putString(key, jsonFavorites).apply()
//    }
//
//    /**
//     *  Functions for writing list in the sharedPreferences
//     * */
    fun <T> saveList(list: ArrayList<T>, key: String?) {
        val gson = Gson()
        val jsonFavorites = gson.toJson(list)
        sharedPreferences.edit().putString(key, jsonFavorites).apply()
    }

    fun getBookMarkList(key: String?): ArrayList<BookmarkModel> {
        return sharedPreferences.getString(
            key, ""
        )?.let {
            if (it.isNotEmpty()) {
                val gson = Gson()
                val listType = object : TypeToken<ArrayList<BookmarkModel>>() {}.type
                gson.fromJson(
                    it,
                    listType
                )
            } else {
                arrayListOf()
            }
        } ?: arrayListOf()
    }

    /**
     *  Functions for reading float in the sharedPreferences
     * */
    fun getLoc(key: String, defval: Float): Float {
        return sharedPreferences.getFloat(key, defval)
    }

    /**
     *  Functions for reading int in the sharedPreferences
     * */
    fun getInt(key: String, defval: Int): Int {
        return sharedPreferences.getInt(key, defval)
    }

    /**
     *  Functions for reading int in the sharedPreferences
     * */
    fun getIntOther(key: String, defval: Int): Int {
        return sharedPreferences.getInt(key, defval)
    }

    /**
     *  Functions for reading string in the sharedPreferences
     * */
    fun getAppString(key: String, defval: String): String? {
        return sharedPreferences.getString(key, defval)
    }

    /**
     *  Functions for reading string in the sharedPreferences
     * */
    fun getString(key: String, defval: String): String? {
        return sharedPreferences.getString(key, defval)
    }

    /**
     *  Functions for reading long in the sharedPreferences
     * */
    fun getLong(key: String, defval: Long): Long {
        return sharedPreferences.getLong(key, defval)
    }
    fun getDouble(key: String, defval: Float): Float {
        return sharedPreferences.getFloat(key, defval)
    }
    fun setDouble(key: String, value: Double) {
        sharedPreferences.edit().putFloat(
            key, value.toFloat()
        ).apply()
    }

    /**
     *  Functions for reading boolean in the sharedPreferences
     * */
    fun getBol(key: String, defval: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defval)
    }

    /**
     *  Functions for reading boolean in the sharedPreferences
     * */
    fun getBolOther(key: String, defval: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defval)
    }

    /**
     *  Functions for reading object in the sharedPreferences
     * */
//    fun getObject(KEY: String?, model: Class<*>): Any? {
//        return if (sharedPreferences.contains(KEY)) {
//            val jsonFavorites = sharedPreferences.getString(KEY, null)
//            val gson = Gson()
//            val favoriteItems: Any? = gson.fromJson(
//                jsonFavorites,
//                model
//            )
//            favoriteItems
//        } else {
//            null
//        }
//    }

    /**
     *  Functions for reading string in the sharedPreferences
     * */
    fun getLanguage(key: String, defval: String): String? {
        return sharedPreferences.getString(key, defval)
    }

    /**
     *  Functions for clearing the sharedPreferences
     * */
    fun clearAllPreferences() {
        sharedPreferences.edit().clear().apply()
    }

    fun clearKey(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    /**
     *  Functions for clearing the sharedPreferences
     * */
    fun removeAll() {
        sharedPreferences.edit().clear().apply()
    }

    fun checkKeyExits(key: String) = sharedPreferences.contains(key)

}