package com.quran.rabiulqaloob.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.quran.rabiulqaloob.models.PlayerModel
import com.quran.rabiulqaloob.utils.Constants
import com.quran.rabiulqaloob.utils.PreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    application: Application,
    preferenceHelper: PreferenceHelper
) : AndroidViewModel(application) {

    private val _playerModel: MutableLiveData<PlayerModel> = MutableLiveData(PlayerModel(isPlayedByUser = false))
    val playerModel: LiveData<PlayerModel> = _playerModel

    init {
        val surahNumber =
            preferenceHelper.getString(
                Constants.SURAH_NUMBER,
                _playerModel.value?.currentSurah ?: "0"
            ) ?: _playerModel.value?.currentSurah ?: "0"

        val surahProgress =
            preferenceHelper.getInt(
                Constants.PROGRESS,
                _playerModel.value?.surahProgress ?: 0
            )
        _playerModel.postValue(
            PlayerModel(
                surahNumber,
                surahProgress,
                surahNumber != "0",
                isPlayedByUser = false
            )
        )
    }

    fun setPlayerModel(
        playerModel: PlayerModel
    ){
        _playerModel.postValue(playerModel)
    }

}