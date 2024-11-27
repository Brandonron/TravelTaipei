package com.example.traveltaipei.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.traveltaipei.data.attractions.TravelAllBody
import com.example.traveltaipei.data.events.NewsBody
import com.example.traveltaipei.data.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class ServiceViewModel(private val repo: TravelRepository) : ViewModel() {

    fun callTravelAll(lang: String): Flow<PagingData<TravelAllBody>> =
        repo.travelAll(lang).cachedIn(viewModelScope)

    fun callNews(lang: String): Flow<PagingData<NewsBody>> =
        repo.news(lang).cachedIn(viewModelScope)
}