package com.djyoo.smartnews.domain.usecase

import com.djyoo.smartnews.domain.engine.RecommendationEngine
import com.djyoo.smartnews.domain.model.Article
import com.djyoo.smartnews.domain.repository.ArticleRepository
import com.djyoo.smartnews.domain.repository.UserProfileRepository

class GetRecommendationsUseCase(
    private val articleRepository: ArticleRepository,
    private val userProfileRepository: UserProfileRepository,
    private val recommendationEngine: RecommendationEngine,
) {
    suspend operator fun invoke(): List<Article> = TODO("Not implemented")
}
