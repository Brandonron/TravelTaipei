package com.example.traveltaipei.viewmodel

import androidx.annotation.StringDef
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.traveltaipei.data.attractions.TravelAllBody

class DataViewModel : ViewModel() {

    @StringDef(
        value = [
            LangType.TW,
            LangType.CN,
            LangType.EN,
            LangType.JA,
            LangType.KO,
            LangType.ES,
            LangType.ID,
            LangType.TH,
            LangType.VI,
        ]
    )

    annotation class LangType {
        companion object {
            const val TW = "zh-tw"
            const val CN = "zh-cn"
            const val EN = "en"
            const val JA = "ja"
            const val KO = "ko"
            const val ES = "es"
            const val ID = "id"
            const val TH = "th"
            const val VI = "vi"
        }
    }

    val toolbarTitle = MutableLiveData<String?>()

    val languageCode = MutableLiveData<String?>()

    val selectDestinationsData = MutableLiveData<TravelAllBody?>()

    fun updateLanguageCode(@LangType code: String) {
        languageCode.value = code// UI Thread
        languageCode.postValue(code)// Worker Thread
    }

    fun updateToolbarTitle(title: String?) {
        toolbarTitle.value = title // UI Thread
        toolbarTitle.postValue(title)// Worker Thread
    }

    fun updateDestinationsData(data: TravelAllBody?) {
        selectDestinationsData.value = data // UI Thread
        selectDestinationsData.postValue(data)// Worker Thread
    }
}