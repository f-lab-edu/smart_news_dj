package com.djyoo.smartnews.domain.usecase

import com.djyoo.smartnews.domain.model.Article
import com.djyoo.smartnews.domain.repository.ArticleRepository

class LoadArticleDetailUseCase(
    private val articleRepository: ArticleRepository,
) {
    suspend operator fun invoke(articleId: String): Article? = articleRepository.getArticleById(articleId)
}
