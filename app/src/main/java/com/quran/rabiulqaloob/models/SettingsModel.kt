package com.quran.rabiulqaloob.models

import com.quran.rabiulqaloob.generics.ListItemViewModel

data class SettingsModel(
    val name: String = "",
    val settingType: SettingsType = SettingsType.LANGUAGE
) : ListItemViewModel()

enum class SettingsType {
    LANGUAGE,
    RATE_APP,
    PRIVACY_POLICY
}
