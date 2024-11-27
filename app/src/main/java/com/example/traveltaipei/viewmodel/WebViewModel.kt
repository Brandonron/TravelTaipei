package com.example.traveltaipei.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WebViewModel : ViewModel() {

    val webUrl = MutableLiveData<String?>()

    fun updateURL(url: String?) {
        webUrl.value = url// UI Thread
        this.webUrl.postValue(url)// Worker Thread
    }
}