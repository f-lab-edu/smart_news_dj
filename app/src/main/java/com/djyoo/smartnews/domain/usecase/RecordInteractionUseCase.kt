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
        TODO("Not implemented")
    }
}
