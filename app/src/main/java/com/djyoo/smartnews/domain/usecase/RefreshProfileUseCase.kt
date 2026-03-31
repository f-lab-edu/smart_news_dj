package com.djyoo.smartnews.domain.usecase

import com.djyoo.smartnews.domain.policy.DecayPolicy
import com.djyoo.smartnews.domain.repository.UserProfileRepository

class RefreshProfileUseCase(
    private val userProfileRepository: UserProfileRepository,
    private val decayPolicy: DecayPolicy,
) {
    suspend operator fun invoke(keywordScores: Map<String, Double>) {
        TODO("Not implemented")
    }

    private companion object {
        const val KEYWORD_SCORE_THRESHOLD: Double = 0.05
        const val TOP_K_KEYWORDS: Int = 20
    }
}
