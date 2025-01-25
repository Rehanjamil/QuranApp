package com.quran.rabiulqaloob.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.media.RingtoneManager
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.Prayer
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.quran.rabiulqaloob.BaseFragment
import com.quran.rabiulqaloob.R
import com.quran.rabiulqaloob.databinding.FragmentPrayerTimesBinding
import com.quran.rabiulqaloob.ui.viewmodels.PrayerViewModel
import com.quran.rabiulqaloob.utils.Constants
import com.quran.rabiulqaloob.utils.capitalizeWords
import java.text.SimpleDateFormat
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs


class PrayerTimesFragment : BaseFragment() {

    private val binding: FragmentPrayerTimesBinding by lazy {
        FragmentPrayerTimesBinding.inflate(layoutInflater)
    }
    private var adjustedDate: HijrahDate? = null
    private var factor: Long = 0L
    private var christianDateFactor: Long = 0L
    private val prayerViewModel by viewModels<PrayerViewModel>()

    @SuppressLint("MissingPermission")
    private val launcher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (it.containsKey(Manifest.permission.ACCESS_COARSE_LOCATION) || it.containsKey(Manifest.permission.ACCESS_FINE_LOCATION)) {
            val permissionState = it[Manifest.permission.ACCESS_COARSE_LOCATION] ?: true &&
                    it[Manifest.permission.ACCESS_FINE_LOCATION] ?: true
            if (
                permissionState
            ) {
                val locationRequest = LocationRequest.create().apply {
                    interval = 10000 // Update interval in milliseconds (10 seconds)
                    fastestInterval = 5000 // Fastest update interval in milliseconds (5 seconds)
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY // Use high accuracy
                }

                val fusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(requireActivity())

                if (ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return@registerForActivityResult
                } else {
                    fusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        object : LocationCallback() {
                            override fun onLocationResult(p0: LocationResult) {
                                val callback = this
                                p0 ?: return
                                fusedLocationClient.removeLocationUpdates(callback)

                                for (location in p0.locations) {
                                    // Handle the user's current location (latitude: location.latitude, longitude: location.longitude)
                                    // Stop location updates if you have received the required location
                                    prayerViewModel.latitude = location.latitude
                                    prayerViewModel.longitude = location.longitude
                                    prayerViewModel.setLocationAddress()
                                    preferenceHelper.setString(
                                        Constants.LATITUDE,
                                        location.latitude.toString()
                                    )
                                    preferenceHelper.setString(
                                        Constants.LONGITUDE,
                                        location.longitude.toString()
                                    )
                                }


                            }
                        },
                        Looper.getMainLooper()
                    )
                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.isFajarNotificationEnabled = preferenceHelper.getBol(
            Constants.FAJAR_NOTIFICATION,
            false
        )
        binding.isSunriseNotificationEnabled = preferenceHelper.getBol(
            Constants.SUNRISE_NOTIFICATION,
            false
        )
        binding.isJumaNotificationEnabled = preferenceHelper.getBol(
            Constants.JUMA_NOTIFICATION,
            false
        )
        binding.isAsrNotificationEnabled = preferenceHelper.getBol(
            Constants.ASR_NOTIFICATION,
            false
        )
        binding.isMaghribNotificationEnabled = preferenceHelper.getBol(
            Constants.MAGHRIB_NOTIFICATION,
            false
        )
        binding.isIshaNotificationEnabled = preferenceHelper.getBol(
            Constants.ISHA_NOTIFICATION,
            false
        )


        factor = preferenceHelper.getLong(Constants.DATE_ADJUSTMENT, 0L)
        setUpUi()
        setObserver()
        getDeviceCurrentLocation()

    }

    private fun setObserver() {
        prayerViewModel.locationAddress.observe(viewLifecycleOwner) {
            binding.tvLocation.text = it ?: ""
        }
    }

    private fun getDeviceCurrentLocation() {
        launcher.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            )
        )
    }

    private fun setUpClickListeners() {
        binding.ivMinus.setOnClickListener {
            factor -= 1L
            christianDateFactor -= 1L
            setUpDates()
        }

        binding.ivPlus.setOnClickListener {
            factor += 1L
            christianDateFactor += 1L
            setUpDates()
        }

        binding.tvContinue.setOnClickListener {
            findNavController()
                .navigate(
                    R.id.action_prayerTimesFragment_to_prayerSettingsFragment
                )
        }

        binding.tvSettings.setOnClickListener {
            findNavController()
                .navigate(
                    R.id.action_prayerTimesFragment_to_prayerSettingsFragment
                )
        }

        binding.ivArrowBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.ivFajrNotification.setOnClickListener {
            binding.isFajarNotificationEnabled = binding.isFajarNotificationEnabled?.not()
            preferenceHelper.setBol(
                Constants.SUNRISE_NOTIFICATION,
                binding.isFajarNotificationEnabled ?: false
            )

        }
        binding.ivSunriseNotification.setOnClickListener {
            binding.isSunriseNotificationEnabled = binding.isSunriseNotificationEnabled?.not()
            preferenceHelper.setBol(
                Constants.SUNRISE_NOTIFICATION,
                binding.isSunriseNotificationEnabled ?: false
            )
        }
        binding.ivJumaNotification.setOnClickListener {
            binding.isJumaNotificationEnabled = binding.isJumaNotificationEnabled?.not()
            preferenceHelper.setBol(
                Constants.SUNRISE_NOTIFICATION,
                binding.isJumaNotificationEnabled ?: false
            )
        }
        binding.ivAsrNotification.setOnClickListener {
            binding.isAsrNotificationEnabled = binding.isAsrNotificationEnabled?.not()
            preferenceHelper.setBol(
                Constants.SUNRISE_NOTIFICATION,
                binding.isAsrNotificationEnabled ?: false
            )
        }
        binding.ivMaghribNotification.setOnClickListener {
            binding.isMaghribNotificationEnabled = binding.isMaghribNotificationEnabled?.not()
            preferenceHelper.setBol(
                Constants.SUNRISE_NOTIFICATION,
                binding.isMaghribNotificationEnabled ?: false
            )
        }
        binding.ivIshaNotification.setOnClickListener {
            binding.isIshaNotificationEnabled = binding.isIshaNotificationEnabled?.not()
            preferenceHelper.setBol(
                Constants.SUNRISE_NOTIFICATION,
                binding.isIshaNotificationEnabled ?: false
            )
        }
        binding.ivRefreshLocation.setOnClickListener {
            getDeviceCurrentLocation()
        }

        binding.ivFajarSettings.setOnClickListener {

        }
        binding.ivSunriseSettings.setOnClickListener {

        }
        binding.ivJumaSettings.setOnClickListener {

        }
        binding.ivAsrSettings.setOnClickListener {

        }
        binding.ivMaghribSettings.setOnClickListener {

        }
        binding.ivIshaSettings.setOnClickListener {

        }
    }

    private fun setUpUi() {
        setUpDates()
        setUpClickListeners()
        setCurrentCalculationMethod()
        binding.tvLocation.text = LocationHelper.getAddressFromLatLng(
            requireActivity(),
            prayerViewModel.latitude,
            prayerViewModel.longitude
        )
        prayerViewModel.startUpdatingPrayerTime { stringBuilder, s ->
            binding.tvPrayerTimeRemaining.text = "$s prayer in $stringBuilder"
        }
    }

    private fun setCurrentCalculationMethod() {
        binding.ivPlus.rotation = if (Locale.getDefault().language in listOf("ar", "ur")){
            90f
        } else {
            270f
        }
        binding.ivMinus.rotation = if (Locale.getDefault().language in listOf("ar", "ur")){
            270f
        } else {
            90f
        }
        val methodName: String = preferenceHelper.getString(
            Constants.CALCULATION_METHOD,
            CalculationMethod.MUSLIM_WORLD_LEAGUE.name
        ) ?: "Muslim World League"
        binding.tvMethod.text = "Based on: ${
            methodName.replace("_", " ").capitalizeWords()
        }\n${getGMTOffSet()} Times may vary"
    }

    private fun getGMTOffSet(): String {
        return TimeZone.getDefault()
            .rawOffset
            .let {
                val hours = it / 3600000
                val minutes = (abs(it / 60000) % 60).toString().padStart(2, '0')
                "GMT " + (if (hours >= 0) "+" else "-") + hours.toString()
                    .padStart(2, '0') + ":" + minutes
            }
    }

    private fun setUpDates() {
        binding.tvHijriDate.text = getIslamicDate()
        binding.tvDate.text = getDate()
        binding.tvPrayerTimeRemaining.isInvisible = christianDateFactor != 0L
        binding.clTimes.alpha = if (christianDateFactor != 0L) 0.5f else 1f
    }

    private fun getDate(): String {
        return SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault()).format(
            Calendar.getInstance().apply {
                this[Calendar.DAY_OF_YEAR] += christianDateFactor.toInt()
            }.time
        )
    }

    private fun getIslamicDate(): String {
        val currentDate = HijrahDate.now()
        adjustedDate = currentDate.plus(
            factor, ChronoUnit.DAYS
        )
        val prayerTimes = prayerViewModel.getPrayerTimesOfDate(
            christianDateFactor.toInt()
        )
        binding.tvFajarTime.text =
            SimpleDateFormat(
                preferenceHelper.getString(
                    Constants.TIME_FORMAT,
                    "hh:mm a"
                ), Locale.ENGLISH
            ).format(
                prayerTimes.fajr
            )

        binding.tvSunriseTime.text =
            SimpleDateFormat(
                preferenceHelper.getString(
                    Constants.TIME_FORMAT,
                    "hh:mm a"
                ), Locale.ENGLISH
            ).format(
                prayerTimes.sunrise
            )

        binding.tvJumaTime.text =
            SimpleDateFormat(
                preferenceHelper.getString(
                    Constants.TIME_FORMAT,
                    "hh:mm a"
                ), Locale.ENGLISH
            ).format(
                prayerTimes.dhuhr
            )

        binding.tvAsrTime.text =
            SimpleDateFormat(
                preferenceHelper.getString(
                    Constants.TIME_FORMAT,
                    "hh:mm a"
                ), Locale.ENGLISH
            ).format(
                prayerTimes.asr
            )

        binding.tvMaghribTime.text =
            SimpleDateFormat(
                preferenceHelper.getString(
                    Constants.TIME_FORMAT,
                    "hh:mm a"
                ), Locale.ENGLISH
            ).format(
                prayerTimes.maghrib
            )

        binding.tvIshaTime.text =
            SimpleDateFormat(
                preferenceHelper.getString(
                    Constants.TIME_FORMAT,
                    "hh:mm a"
                ), Locale.ENGLISH
            ).format(
                prayerTimes.isha
            )

        binding.viewCurrentFajr.isVisible = prayerTimes.currentPrayer() == Prayer.FAJR
        binding.viewCurrentSunrise.isVisible = prayerTimes.currentPrayer() == Prayer.SUNRISE
        binding.viewCurrentJuma.isVisible = prayerTimes.currentPrayer() == Prayer.DHUHR
        binding.viewCurrentAsr.isVisible = prayerTimes.currentPrayer() == Prayer.ASR
        binding.viewCurrentMaghrib.isVisible = prayerTimes.currentPrayer() == Prayer.MAGHRIB
        binding.viewCurrentIsha.isVisible = prayerTimes.currentPrayer() == Prayer.ISHA
        val customRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        return adjustedDate?.let {
            it.format(
                DateTimeFormatter.ofPattern("dd")
            ) + ", " + Constants.hijrahMonthNames[it[ChronoField.MONTH_OF_YEAR] - 1] + " " + it.format(
                DateTimeFormatter.ofPattern("yyyy")
            ) + " AH"
        } ?: ""
    }
}

object LocationHelper {
    fun getAddressFromLatLng(context: Context?, latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context!!, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                val sb = StringBuilder()
                for (i in 0..address.maxAddressLineIndex) {
                    sb.append(address.getAddressLine(i)).append(" ")
                }
                return sb.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "Address not found"
    }
}