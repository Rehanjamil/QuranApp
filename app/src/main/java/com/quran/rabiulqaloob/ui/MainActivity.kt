package com.quran.rabiulqaloob.ui

import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.quran.rabiulqaloob.R
import com.quran.rabiulqaloob.databinding.ActivityMainBinding
import com.quran.rabiulqaloob.models.PlayerModel
import com.quran.rabiulqaloob.ui.viewmodels.PlayerViewModel
import com.quran.rabiulqaloob.utils.Constants
import com.quran.rabiulqaloob.utils.PreferenceHelper
import com.quran.rabiulqaloob.utils.readJsonFromAssets
import com.quran.rabiulqaloob.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : LocalizationActivity() {
    lateinit var binding: ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler()
    private var playerModel: PlayerModel = PlayerModel()

    @Inject
    lateinit var preferenceHelper: PreferenceHelper
    private lateinit var navController: NavController

    private val playerViewModel by viewModels<PlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment)
            .let {
                navController = it.findNavController()
                it.findNavController().let {
                    it.addOnDestinationChangedListener { controller, destination, arguments ->

                        when (destination.id) {
                            in listOf(
                                R.id.homeFragment,
                                R.id.juzzListingFragment,
                                R.id.surahListingFragment,
                                R.id.bookmarkListingFragment,
                                R.id.splashFragment,
                                R.id.prayerTimesFragment,
                                R.id.prayerSettingsFragment,
                            ) -> {
                                setUpPlayerVisibility()
                                window.statusBarColor =
                                    ResourcesCompat.getColor(
                                        resources,
                                        R.color.colorPrimary,
                                        null
                                    )
                            }

                            (R.id.readerFragment) -> {
                                window.statusBarColor =
                                    Color.parseColor(
                                        "#A49F94"
                                    )
                            }

                            else -> {
                                window.statusBarColor = Color.BLACK
                            }
                        }

                    }
                }
            }

        binding.seekbarProgress.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2) {
                    // Update the MediaPlayer playback position when the SeekBar is dragged
                    mediaPlayer?.let {
                        if (it.isPlaying.not()) {
                            startMedia()
                        }
                        mediaPlayer?.seekTo(p1)
                    }
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
        binding.buttonPlayPause1.setOnClickListener {
            mediaPlayer?.isPlaying?.let {
                if (it) {
                    pauseMedia()
                } else {
                    startMedia()
                }
            }
        }

        binding.buttonNext1.setOnClickListener {
            handler.removeCallbacksAndMessages(null)
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.release()
            }
            proceedToNextSurah()
        }
        binding.ivHide1.setOnClickListener {
            preferenceHelper.clearKey(Constants.SURAH_NUMBER)
            playerViewModel.setPlayerModel(
                PlayerModel()
            )
        }

        setObserver()
    }

    private fun setObserver() {
        playerViewModel.playerModel.observe(this) {
            playerModel = it
            checkPlayerConditions()
        }
    }

    private fun checkPlayerConditions() {
        if (playerModel.currentSurah != "0") {
            setUpMediaPlayer()
            binding.buttonNext1.isEnabled = playerModel.currentSurah.toInt() != 114
            binding.buttonNext1.alpha =
                if (playerModel.currentSurah.toInt() != 114) 1f else 0.5f
            binding.seekbarProgress.progress = playerModel.surahProgress
            readJsonFromAssets(
                "surah.json"
            ).apply {
                val surah = this.find {
                    it.number == playerModel.currentSurah.toInt()
                }
                surah?.let {
                    binding.tvNameTitle1.text = it.englishName
                }
            }
        } else {
            mediaPlayer?.isPlaying?.takeIf { it }?.let {
                handler.removeCallbacksAndMessages(null)
                mediaPlayer?.release()
            }
        }
        setUpPlayerVisibility()
    }

    private fun setUpPlayerVisibility() {
        binding.cvPlayer.isVisible =
            playerModel.isPlayerShown && navController.currentDestination?.id in listOf(
                R.id.homeFragment,
                R.id.juzzListingFragment,
                R.id.surahListingFragment,
                R.id.bookmarkListingFragment,
                R.id.prayerTimesFragment,
                R.id.prayerSettingsFragment,
                R.id.readerFragment
            )
    }

    private fun setUpMediaPlayer() {
        initMediaPlayer()
        setPlayerAttributes(
            playerModel.currentSurah.toInt()
        )
    }

    private fun startMedia() {
        mediaPlayer?.start()
        binding.buttonPlayPause1.isSelected = true
    }

    private fun pauseMedia() {
        binding.buttonPlayPause1.isSelected = false
        mediaPlayer?.pause()
    }

    private fun initMediaPlayer() {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
    }

    private fun setPlayerAttributes(fileName: Int) {
        binding.clp.show()

        val audioUrl =
            "${"https://download.quranicaudio.com/quran/abdurrahmaan_as-sudays/"}${
                String.format(
                    Locale.ENGLISH,
                    "%03d",
                    fileName
                )
            }.mp3"
        Log.e("setPlayerAttributes: ", audioUrl)
        mediaPlayer?.apply {
            setDataSource(audioUrl)
            setOnCompletionListener {
                proceedToNextSurah()
            }
            setOnPreparedListener {
                // Start playing audio when MediaPlayer is prepared
                binding.seekbarProgress.isInvisible = false
                binding.seekbarProgress.max = mediaPlayer!!.duration
                binding.clp.hide()
                updateSeekBar()
                startMedia()
                if (playerModel.isPlayedByUser.not()) {
                    binding.buttonPlayPause1.isSelected = false
                    binding.seekbarProgress.progress = playerModel.surahProgress
                    seekTo(playerModel.surahProgress)
                    pause()
                }
            }

            setOnErrorListener { mediaPlayer, what, extra ->
                checkError(what)
                true
            }

            // Update SeekBar position every 100 milliseconds
            handler.postDelayed(object : Runnable {
                override fun run() {
                    updateSeekBar()
                    handler.postDelayed(this, 100)
                }
            }, 100)
            prepareAsync() // Prepare the MediaPlayer asynchronously
        }

    }

    private fun proceedToNextSurah() {
        playerViewModel.setPlayerModel(
            PlayerModel(
                playerModel.currentSurah.toInt().plus(1).toString(),
                isPlayerShown = true
            )
        )
    }

    private fun checkError(what: Int) {
        binding.clp.hide()
        binding.seekbarProgress.isInvisible = false
        when (what) {
            MediaPlayer.MEDIA_ERROR_IO -> {
                // Handle network-related I/O error
                // This could indicate network issues, such as timeouts or connectivity problems
                // You can display an error message to the user or retry the operation
                showToast(
                    "No network connection"
                )
            }

            MediaPlayer.MEDIA_ERROR_TIMED_OUT -> {
                // Handle timeout error
                // This indicates a timeout occurred while trying to connect to the media server
                // You can display an error message to the user or retry the operation
            }

            MediaPlayer.MEDIA_ERROR_UNSUPPORTED -> {
                // Handle unsupported media format error
                // This indicates that the media format is not supported by the player or the device
                // You can display an error message to the user or prompt them to use a different media file
            }

            else -> {
                // Handle other MediaPlayer errors here
            }
        }
    }

    private fun updateSeekBar() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                preferenceHelper.setInt(
                    Constants.PROGRESS,
                    it.currentPosition
                )
                binding.seekbarProgress.progress = it.currentPosition
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release() // Release the MediaPlayer when the activity is destroyed
        handler.removeCallbacksAndMessages(null)
    }
}