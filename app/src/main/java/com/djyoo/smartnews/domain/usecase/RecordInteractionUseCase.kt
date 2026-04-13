package com.djyoo.smartnews.domain.usecase

import com.djyoo.smartnews.domain.model.Interaction
import com.djyoo.smartnews.domain.repository.ArticleRepository
import com.djyoo.smartnews.domain.repository.InteractionRepository
import com.djyoo.smartnews.domain.scorer.ScoreCalculator

class RecordInteractionUseCase(
    private val interactionRepository: InteractionRepository,
    private val articleRepository: ArticleRepository,
    private val scoreCalculator: ScoreCalculator,
    private val refreshProfileUseCase: RefreshProfileUseCase,
) {
    suspend operator fun invoke(interaction: Interaction) {
        interactionRepository.insertInteraction(interaction)
        val article = articleRepository.getArticleById(interaction.articleId) ?: return
        if (article.keywords.isEmpty()) return
        val baseScore = scoreCalculator.calculate(interaction)
        val n = article.keywords.size
        val perKeyword = baseScore / n
        val keywordScores = article.keywords.associateWith { perKeyword }
        refreshProfileUseCase(keywordScores)
    }
}
