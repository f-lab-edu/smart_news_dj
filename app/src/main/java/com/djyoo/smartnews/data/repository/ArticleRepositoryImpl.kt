package com.djyoo.smartnews.data.repository

import com.djyoo.smartnews.data.local.dao.ArticleDao
import com.djyoo.smartnews.data.remote.api.NaverNewsApi
import com.djyoo.smartnews.domain.keyword.KeywordExtractor
import com.djyoo.smartnews.domain.model.Article
import com.djyoo.smartnews.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ArticleRepositoryImpl(
    private val articleDao: ArticleDao,
    private val naverNewsApi: NaverNewsApi,
    private val keywordExtractor: KeywordExtractor,
) : ArticleRepository {
    override fun observeArticles(limit: Int): Flow<List<Article>> = flowOf(emptyList())

    override suspend fun fetchAndStoreNews(
        query: String,
        start: Int,
        display: Int,
    ): Int = TODO("Not implemented")

    override suspend fun getArticlesSnapshot(limit: Int): List<Article> = TODO("Not implemented")
}
