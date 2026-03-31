package com.djyoo.smartnews.domain.model

data class Article(
    val id: String,
    val title: String,
    val description: String,
    val link: String,
    val originalLink: String,
    val pubDate: Long,
    val keywords: List<String>,
    val fetchedAt: Long,
)
