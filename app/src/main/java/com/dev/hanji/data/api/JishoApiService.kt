package com.dev.hanji.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface JishoApiService {
    @GET("api/v1/search/words")
    suspend fun getJisho(@Query("keyword") keyword: String): JishoResponse
}