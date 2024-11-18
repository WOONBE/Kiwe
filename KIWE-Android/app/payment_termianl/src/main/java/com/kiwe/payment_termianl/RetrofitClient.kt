package com.kiwe.payment_termianl

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    fun create(): ApiService {
        val baseUrl = "https://${BuildConfig.BASE_URL}"
//        val baseUrl = "https://www.naver.com"
        val retrofit =
            Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .client(
                    OkHttpClient
                        .Builder()
                        .addInterceptor(
                            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY),
                        ).build(),
                ).addConverterFactory(GsonConverterFactory.create())
                .build()

        return retrofit.create(ApiService::class.java)
    }
}
