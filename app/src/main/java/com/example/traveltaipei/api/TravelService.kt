package com.example.traveltaipei.api

import com.example.traveltaipei.data.attractions.TravelAllBody
import com.example.traveltaipei.data.events.ActivityBody
import com.example.traveltaipei.data.events.CalendarBody
import com.example.traveltaipei.data.events.NewsBody
import com.example.traveltaipei.data.events.ThemeBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface TravelService {

    /***
    Attractions 遊憩景點
     */
    @GET("{lang}/Attractions/All")
    suspend fun getTravelAll(
        @Path("lang") lang: String,
        @Query("page") page: Int = 1
    ): BaseResponse<TravelAllBody>

    /***
    Events活動資訊
     */
    @GET("{lang}/Events/News") //最新消息（文字）
    suspend fun getNews(
        @Path("lang") lang: String,
        @Query("page") page: Int = 1
    ): BaseResponse<NewsBody>

    @GET("{lang}/Events/Activity") //活動展演
    fun getActivity(@Path("lang") lang: String): BaseResponse<ActivityBody>

    @GET("{lang}/Events/Calendar") //活動年曆
    fun getCalendar(@Path("lang") lang: String): BaseResponse<CalendarBody>

    /***
    Media 影音刊物
     */

    /***
    Miscellaneous 其他
     */

    /***
    Tours 玩樂台北
     */
    @GET("{lang}/Tours/Theme") //主題遊程
    fun getTheme(@Path("lang") lang: String):BaseResponse<ThemeBody>
}