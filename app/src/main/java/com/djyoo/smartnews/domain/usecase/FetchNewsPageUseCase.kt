package com.djyoo.smartnews.domain.usecase

import com.djyoo.smartnews.domain.model.Article
import com.djyoo.smartnews.domain.repository.ArticleRepository

class FetchNewsPageUseCase(
    private val articleRepository: ArticleRepository,
) {
    suspend operator fun invoke(
        query: String,
        start: Int,
        display: Int,
    ): List<Article> = articleRepository.fetchNewsPage(query = query, start = start, display = display)
}
