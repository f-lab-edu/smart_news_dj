package com.djyoo.smartnews.domain.usecase

import com.djyoo.smartnews.domain.repository.ArticleRepository

class FetchNewsUseCase(
    private val articleRepository: ArticleRepository,
) {
    suspend operator fun invoke(
        query: String = "뉴스",
        start: Int,
        display: Int = 100,
    ): Int = TODO("Not implemented")
}
