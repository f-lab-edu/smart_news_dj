package com.djyoo.smartnews.domain.repository

import com.djyoo.smartnews.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    fun observeArticles(limit: Int = 100): Flow<List<Article>>

    suspend fun fetchNewsPage(
        query: String,
        start: Int,
        display: Int,
    ): List<Article>

    suspend fun fetchAndStoreNews(
        query: String,
        start: Int,
        display: Int,
    ): Int

    suspend fun getArticlesSnapshot(limit: Int = 100): List<Article>
}
