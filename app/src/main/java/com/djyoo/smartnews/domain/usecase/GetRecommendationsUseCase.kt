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
    suspend operator fun invoke(): List<Article> {
        val articles = articleRepository.getArticlesSnapshot(limit = 100)
        val profile = userProfileRepository.getUserKeywordsSnapshot(limit = 20)
        return recommendationEngine.recommend(articles = articles, profile = profile)
    }
}
