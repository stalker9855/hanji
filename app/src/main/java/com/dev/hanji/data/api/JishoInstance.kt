package com.dev.hanji.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object JishoInstance {
    private const val BASE_URL = "https://jisho.org/"

    val api: JishoApiService by lazy {
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JishoApiService::class.java)
    }
}