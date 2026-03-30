package com.djyoo.smartnews.domain.keyword

interface KeywordExtractor {
    suspend fun extract(
        title: String,
        description: String,
    ): List<String>
}
