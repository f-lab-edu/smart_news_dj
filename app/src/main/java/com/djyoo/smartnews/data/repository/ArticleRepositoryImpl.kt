package com.djyoo.smartnews.data.repository

import com.djyoo.smartnews.BuildConfig
import com.djyoo.smartnews.data.local.dao.ArticleDao
import com.djyoo.smartnews.data.local.mapper.toDomain
import com.djyoo.smartnews.data.local.mapper.toEntity
import com.djyoo.smartnews.data.local.mapper.toKeywordCrossRefs
import com.djyoo.smartnews.data.remote.api.NaverNewsApi
import com.djyoo.smartnews.data.remote.mapper.toDomain
import com.djyoo.smartnews.domain.keyword.KeywordExtractor
import com.djyoo.smartnews.domain.model.Article
import com.djyoo.smartnews.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ArticleRepositoryImpl(
    private val articleDao: ArticleDao,
    private val naverNewsApi: NaverNewsApi,
    private val keywordExtractor: KeywordExtractor,
) : ArticleRepository {
    override fun observeArticles(limit: Int): Flow<List<Article>> =
        articleDao.observeArticlesWithKeywords(limit).map { list -> list.map { it.toDomain() } }

    override suspend fun fetchNewsPage(
        query: String,
        start: Int,
        display: Int,
    ): List<Article> {
        val response =
            naverNewsApi.getNews(
                clientId = BuildConfig.NAVER_CLIENT_ID,
                clientSecret = BuildConfig.NAVER_CLIENT_SECRET,
                query = query,
                display = display,
                start = start,
            )
        val fetchedAt = System.currentTimeMillis()
        return response.items.map { item ->
            item.toDomain(keywords = emptyList(), fetchedAt = fetchedAt)
        }
    }

    override suspend fun fetchAndStoreNews(
        query: String,
        start: Int,
        display: Int,
    ): Int {
        val articles = fetchNewsPage(query = query, start = start, display = display)

        articleDao.upsertArticles(articles.map { it.toEntity() })
        articleDao.upsertArticleKeywords(articles.flatMap { it.toKeywordCrossRefs() })
        articleDao.trimToMaxCount(maxCount = 100)
        return articles.size
    }

    override suspend fun getArticlesSnapshot(limit: Int): List<Article> = articleDao.getArticlesWithKeywords(limit).map { it.toDomain() }

    override suspend fun getArticleById(id: String): Article? = articleDao.getArticleWithKeywordsById(id)?.toDomain()
}
