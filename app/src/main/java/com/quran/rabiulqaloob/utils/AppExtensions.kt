package com.quran.rabiulqaloob.utils

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quran.rabiulqaloob.R
import com.quran.rabiulqaloob.models.SurahModel


fun Activity.readJsonFromAssets(fileName: String): ArrayList<SurahModel> {
    val jsonString: String
    try {
        val inputStream = assets.open(fileName)
        jsonString = inputStream.bufferedReader().use { it.readText() }
    } catch (e: Exception) {
        e.printStackTrace()
        return arrayListOf()
    }

    val gson = Gson()
    val listType = object : TypeToken<ArrayList<SurahModel>>() {}.type
    return gson.fromJson(jsonString, listType)
}

fun Activity.showToast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun String.capitalizeWords(): String = split(" ").joinToString(" ") {
    it.replaceFirstChar {
        if (it.isLowerCase()) {
            it.titlecase()
        } else it.toString()
    }
}

fun Activity.openAndClearActivity( calledActivity: Class<*>) {
    val myIntent = Intent(this, calledActivity)
    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    this.startActivity(myIntent)
}