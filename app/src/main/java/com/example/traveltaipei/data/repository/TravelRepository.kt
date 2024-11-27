package com.example.traveltaipei.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.traveltaipei.api.TravelService
import com.example.traveltaipei.data.attractions.TravelAllBody
import com.example.traveltaipei.data.events.NewsBody
import kotlinx.coroutines.flow.Flow

class TravelRepository(private val service: TravelService) {
    fun travelAll(
        lang: String
    ): Flow<PagingData<TravelAllBody>> =
        Pager(
            config = PagingConfig(30),
            pagingSourceFactory = { RepoTravelAllPagingSource(service, lang) }
        ).flow

    fun news(
        lang: String
    ): Flow<PagingData<NewsBody>> =
        Pager(
            config = PagingConfig(30),
            pagingSourceFactory = { RepoNewsAllPagingSource(service, lang) }
        ).flow

    inner class RepoTravelAllPagingSource(
        private val apiService: TravelService,
        private val lang: String,
    ) :
        PagingSource<Int, TravelAllBody>() {

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TravelAllBody> {
            return try {
                val page = params.key ?: 1
                val repoRec = apiService.getTravelAll(lang, page)
                val repoItems = repoRec.data
                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (repoItems.isNotEmpty()) page + 1 else null

                LoadResult.Page(repoItems, prevKey, nextKey)
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, TravelAllBody>): Int? = null
    }

    inner class RepoNewsAllPagingSource(
        private val apiService: TravelService,
        private val lang: String,
    ) :
        PagingSource<Int, NewsBody>() {

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsBody> {
            return try {
                val page = params.key ?: 1
                val repoRec = apiService.getNews(lang, page)
                val repoItems = repoRec.data
                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (repoItems.isNotEmpty()) page + 1 else null

                LoadResult.Page(repoItems, prevKey, nextKey)
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, NewsBody>): Int? = null
    }
}

