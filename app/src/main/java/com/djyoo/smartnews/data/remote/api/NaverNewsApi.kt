package com.djyoo.smartnews.data.remote.api

import com.djyoo.smartnews.data.remote.dto.NaverNewsResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NaverNewsApi {
    @GET("v1/search/news.json")
    suspend fun getNews(
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
        @Query("query") query: String,
        @Query("display") display: Int,
        @Query("start") start: Int,
        @Query("sort") sort: String = "date",
    ): NaverNewsResponse
}
