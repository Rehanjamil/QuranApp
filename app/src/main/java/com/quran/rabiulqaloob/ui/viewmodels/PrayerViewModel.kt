package com.quran.rabiulqaloob.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.CalculationParameters
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.Prayer
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.data.DateComponents
import com.quran.rabiulqaloob.ui.fragments.LocationHelper
import com.quran.rabiulqaloob.utils.Constants
import com.quran.rabiulqaloob.utils.PreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class PrayerViewModel @Inject constructor(
    application: Application,
    val preferenceHelper: PreferenceHelper
) : AndroidViewModel(
    application
) {
    var latitude = 31.4992714 // Your latitude
    var longitude = 74.2542153 // Your longitude

    private val _locationAddress = MutableLiveData<String>("")
    val locationAddress: LiveData<String> = _locationAddress
    init {
        latitude = preferenceHelper.getString(
            Constants.LATITUDE,
            latitude.toString()
        )?.toDouble() ?: latitude
        longitude =
            preferenceHelper.getString(
                Constants.LONGITUDE,
                longitude.toString()
            )?.toDouble() ?: longitude
    }

    fun startUpdatingPrayerTime(updateCallback: (StringBuilder, String) -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            while (isActive) {
                // Update your prayer time logic here
                var nextPrayerTime: Date?

                val prayerTimes = getPrayerTimes()

                nextPrayerTime = when (prayerTimes.nextPrayer()) {
                    Prayer.FAJR -> {
                        prayerTimes.fajr
                    }

                    Prayer.DHUHR -> {
                        prayerTimes.dhuhr
                    }

                    Prayer.ASR -> {
                        prayerTimes.asr
                    }

                    Prayer.MAGHRIB -> {
                        prayerTimes.maghrib
                    }

                    Prayer.ISHA -> {
                        prayerTimes.isha
                    }

                    Prayer.SUNRISE -> {
                        prayerTimes.sunrise
                    }

                    else -> {
                        null
                    }
                }
                val timeRemaining = updateNextPrayerTime(nextPrayerTime)

                // Invoke the callback with the formatted time remaining
                updateCallback.invoke(timeRemaining, prayerTimes.nextPrayer().name)

                // Delay for 1 second
                kotlinx.coroutines.delay(1000)
            }
        }
    }

    private fun updateNextPrayerTime(nextPrayerTime: Date?): java.lang.StringBuilder {
        val outputBuilder = StringBuilder("")

        nextPrayerTime?.let {
            val timeRemainingMillis = it.time - System.currentTimeMillis()

            val hours = (timeRemainingMillis / (1000 * 60 * 60)) % 24
            val minutes = (timeRemainingMillis / (1000 * 60)) % 60
            val seconds = (timeRemainingMillis / 1000) % 60

            // Create a StringBuilder to construct the output string

            if (hours > 0) {
                outputBuilder.append("$hours").append("hr")
            }

            if (minutes > 0) {
                if (outputBuilder.isNotEmpty()) {
                    outputBuilder.append(" ")
                }
                outputBuilder.append("$minutes").append("m")
            }

            if (seconds > 0) {
                if (outputBuilder.isNotEmpty()) {
                    outputBuilder.append(" ")
                }
                outputBuilder.append(seconds.toString()).append("s")
            }
            return outputBuilder
        }
        return outputBuilder
    }


    private fun getPrayerDate(): DateComponents {

        val prayer = PrayerTimes(
            Coordinates(latitude, longitude),
            DateComponents.from(
                Date()
            ),
            getCalculationMethod()
        )
        return if (
            Date().after(
                prayer.isha
            )
        ) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            DateComponents.from(
                calendar.time
            )
        } else {
            DateComponents.from(Date())
        }
    }

    fun getPrayerTimesOfDate(
        factor: Int
    ): PrayerTimes {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, factor)
        return PrayerTimes(
            Coordinates(latitude, longitude),
            DateComponents.from(
                calendar.time
            ),
            getCalculationMethod()
        )
    }


    private fun getPrayerTimes(): PrayerTimes {
        return PrayerTimes(
            Coordinates(latitude, longitude),
            getPrayerDate(),
            getCalculationMethod()
        )
    }

    private fun getCalculationMethod(): CalculationParameters {
        val method = preferenceHelper.getString(
            Constants.CALCULATION_METHOD,
            CalculationMethod.MUSLIM_WORLD_LEAGUE.name
        ) ?: CalculationMethod.MUSLIM_WORLD_LEAGUE.name

        return CalculationMethod.valueOf(
            method
        ).parameters
    }


    fun setLocationAddress() {
        val address = LocationHelper.getAddressFromLatLng(
            getApplication(),
            latitude,
            longitude
        )
        _locationAddress.postValue(address)

    }


}