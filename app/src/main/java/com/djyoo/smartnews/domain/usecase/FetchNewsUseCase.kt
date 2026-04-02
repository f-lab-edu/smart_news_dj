package com.djyoo.smartnews.domain.usecase

import com.djyoo.smartnews.domain.model.Article
import com.djyoo.smartnews.domain.repository.ArticleRepository

class FetchNewsUseCase(
    private val articleRepository: ArticleRepository,
) {
    suspend operator fun invoke(
        query: String,
        start: Int,
        display: Int,
    ): List<Article> {
        val fetchedCount = articleRepository.fetchAndStoreNews(query = query, start = start, display = display)
        val limit = minOf((start - 1) + fetchedCount, 100)
        return articleRepository.getArticlesSnapshot(limit = limit)
    }
}
