package com.djyoo.smartnews.domain.usecase

import com.djyoo.smartnews.domain.repository.ArticleRepository
import com.djyoo.smartnews.domain.model.Article

class FetchNewsUseCase(
    private val articleRepository: ArticleRepository,
) {
    suspend operator fun invoke(
        query: String = "뉴스",
        start: Int,
        display: Int = 100,
    ): List<Article> {
        val fetchedCount = articleRepository.fetchAndStoreNews(query = query, start = start, display = display)
        val limit = minOf((start - 1) + fetchedCount, 100)
        return articleRepository.getArticlesSnapshot(limit = limit)
    }
}
