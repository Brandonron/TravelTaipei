package com.example.traveltaipei.di

import android.text.TextUtils
import com.example.traveltaipei.api.APIConfig
import com.example.traveltaipei.api.TravelService
import com.example.traveltaipei.data.repository.TravelRepository
import com.example.traveltaipei.viewmodel.ServiceViewModel
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val mainVmModule = module {
    viewModel { ServiceViewModel(get()) }
}

val apiModule = module {
    single { createOkHttpClient() }
    single { createRetrofit<TravelService>(get(), APIConfig.BASE_URL) }
}

val repositoryModule = module {
    single { TravelRepository(get()) }
}

val diModules = listOf(
    mainVmModule,
    apiModule,
    repositoryModule
)

fun createOkHttpClient(): OkHttpClient {
    class HeaderInterceptor : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()

            val builder = original.newBuilder()
                .header("Content-Type", "application/json")

            val request = builder.build()

            return chain.proceed(request)
        }
    }

    return OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })
        .addInterceptor(object : Interceptor{
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response{
                val original = chain.request()

                HttpLoggingInterceptor
                val builder = original.newBuilder()
                    .header("Accept", "application/json")

                val request = builder.build()

                return chain.proceed(request)
            }
        })
        .connectTimeout(60L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .connectionPool(ConnectionPool(0, 1, TimeUnit.NANOSECONDS))
        .build()
}

inline fun <reified T> createRetrofit(okHttpClient: OkHttpClient, serverUrl: String): T {

    val retrofit = Retrofit.Builder()
        .baseUrl(serverUrl)                                     //設定請求URL
        .client(okHttpClient)                                   //設定OkHttp攔截器
        .addConverterFactory(GsonConverterFactory.create())     //設定解析工具，這裡使用Gson解析，你也可以使用Jackson等
        .build()

    return retrofit.create(T::class.java)
}


