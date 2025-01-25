package com.quran.rabiulqaloob.utils

import com.quran.rabiulqaloob.models.Juzz

object Constants {
    const val FAJAR_NOTIFICATION: String = "fajar_notification"
    const val SUNRISE_NOTIFICATION: String = "sunrise_notification"
    const val JUMA_NOTIFICATION: String = "juma_notification"
    const val ASR_NOTIFICATION: String = "asr_notification"
    const val MAGHRIB_NOTIFICATION: String = "maghrib_notification"
    const val ISHA_NOTIFICATION: String = "isha_notification"
    const val CALCULATION_METHOD: String = "calculation_method_name"
    const val TIME_FORMAT: String = "time_format"
    const val RESOURCE_DOWNLOADED: String = "resource_downloaded"
    const val RESOURCE_PERMISSION: String = "resource_permission"
    const val LANGUAGE = "language"
    const val BOOKMARK = "bookmark"
    const val LATITUDE = "latitude"
    const val LONGITUDE = "longitude"
    const val SURAH_NUMBER = "surah_number"
    const val SURAH_NAME = "surah_name"
    const val CURRENT_READING_INDEX = "current_reading_index"
    const val DATE_ADJUSTMENT = "date_adjustment"
    const val PREF_NAMES: String = "preference"
    const val PROGRESS = "progress"

    val hijrahMonthNames = arrayOf(
        "Muharram", "Safar", "Rabi ul Awal", "Rabi ul Thani",
        "Jumada ul Awal", "Jumada ul Thani", "Rajab", "Sha'ban",
        "Ramadan", "Shawwal", "Dhu al-Qi'dah", "Dhu al-Hijjah"
    )

    val juzzListing = arrayListOf<Juzz>(
        Juzz(
            "Alif Laam Meem",
            "آلم",
            "2",
            "1"
        ),
        Juzz(
            "Sayaqūl",
            "سيقول السفهاء",
            "21",
            "2"
        ),
        Juzz(
            "Tilka-r-rusul",
            "تلك الرسل",
            "39",
            "3"
        ),
        Juzz(
            "Lan Tana Lu",
            "لن تنالوا",
            "57",
            "4"
        ),
        Juzz(
            "W-al-muḥṣanāt",
            "والمحصنات",
            "75",
            "5"
        ),
        Juzz(
            "Lā yuẖibbu-llāh",
            "لا يحب الله",
            "93",
            "6"
        ),
        Juzz(
            "Wa ʾidha samiʿū",
            "وإذا سمعوا",
            "111",
            "7"
        ),
        Juzz(
            "Wa law ʾannanā",
            "ولو أننا",
            "129",
            "8"
        ),
        Juzz(
            "Qāl al-malāʾ",
            "قال الملأ",
            "147",
            "9"
        ),
        Juzz(
            "W-aʿlamū",
            "واعلموا",
            "165",
            "10"
        ),
        Juzz(
            "Yaʾtadhirūna",
            "يعتذرون",
            "183",
            "11"
        ),
        Juzz(
            "Wa mā min dābbah",
            "ومامن دابة",
            "201",
            "12"
        ),
        Juzz(
            "Wa mā ʾubarriʾu",
            "وما أبرئ",
            "219",
            "13"
        ),
        Juzz(
            "Ruba Maʾ",
            "ربما",
            "237",
            "14"
        ),
        Juzz(
            "Subḥāna -lladhi",
            "سبحٰن الذیٓ",
            "255",
            "15"
        ),
        Juzz(
            "Qāl al-malāʾ",
            "قال الملأ",
            "273",
            "16"
        ),
        Juzz(
            "Iqtaraba li-n-nās",
            "اقترب للناس",
            "291",
            "17"
        ),
        Juzz(
            "Qad ʾaflaḥa",
            "قد أفلح",
            "309",
            "18"
        ),
        Juzz(
            "Wa-qāla -lladhīna",
            "وقال الذين",
            "327",
            "19"
        ),
        Juzz(
            "Amman khalaqa",
            "امن خلق",
            "345",
            "20"
        ),
        Juzz(
            "Otlu maa oohiya",
            "اتل مآ اوحی",
            "363",
            "21"
        ),
        Juzz(
            "Wa-man yaqnut",
            "ومن يقنت",
            "381",
            "22"
        ),
        Juzz(
            "Wama liya",
            "وما أبرئ",
            "399",
            "23"
        ),
        Juzz(
            "Fa-man ʾaẓlamu",
            "فمن أظلم",
            "417",
            "24"
        ),
        Juzz(
            "ʾIlaihi yuraddu",
            "إليه يرد",
            "435",
            "25"
        ),
        Juzz(
            "Ḥāʾ Mīm",
            "حـم",
            "453",
            "26"
        ),
        Juzz(
            "Qāla fa-mā khatbukum",
            "قال فما خطبكم",
            "471",
            "27"
        ),
        Juzz(
            "Qad samiʿa -llāhu",
            "قد سمع اللہ",
            "489",
            "28"
        ),
        Juzz(
            "Tabāraka -lladhi",
            "تبٰرک الذی",
            "509",
            "29"
        ),
        Juzz(
            "ʿAmma",
            "عمّ",
            "529",
            "30"
        ),
    )

//    val partsHashMap = HashMap<Int, HashMap<Int, String>>().apply {
//        this[1] = HashMap<Int, String>().apply {
//            this[0] = "2"
//            this[1] = "8"
//            this[2] = "12"
//            this[3] = "16"
//        }
//        this[2] = HashMap<Int, String>().apply {
//            this[0] = "21"
//            this[1] = "25"
//            this[2] = "30"
//            this[3] = "35"
//        }
//        this[3] = HashMap<Int, String>().apply {
//            this[0] = "39"
//            this[1] = "43"
//            this[2] = "48"
//            this[3] = "52"
//        }
//        this[4] = HashMap<Int, String>().apply {
//            this[0] = "57"
//            this[1] = "61"
//            this[2] = "66"
//            this[3] = "70"
//        }
//        this[5] = HashMap<Int, String>().apply {
//            this[0] = "75"
//            this[1] = "79"
//            this[2] = "84"
//            this[3] = "88"
//        }
//        this[6] = HashMap<Int, String>().apply {
//            this[0] = "93"
//            this[1] = "97"
//            this[2] = "102"
//            this[3] = "106"
//        }
//        this[7] = HashMap<Int, String>().apply {
//            this[0] = "93"
//            this[1] = "97"
//            this[2] = "102"
//            this[3] = "106"
//        }
//    }
}

enum class NotificationType(
    value: String
) {
    SILENT("silent"),
    BEEP("beep"),
    AZAN("azan"),
    FULLAZAN("full_azan"),
    VIBRATE("vibrate")
}