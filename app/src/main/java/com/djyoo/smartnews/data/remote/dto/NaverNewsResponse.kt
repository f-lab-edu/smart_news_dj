package com.djyoo.smartnews.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NaverNewsResponse(
    val total: Int,
    val start: Int,
    val display: Int,
    val items: List<NaverNewsItemDto>,
)

data class NaverNewsItemDto(
    val title: String,
    @SerializedName("originallink")
    val originalLink: String,
    val link: String,
    val description: String,
    val pubDate: String,
)
