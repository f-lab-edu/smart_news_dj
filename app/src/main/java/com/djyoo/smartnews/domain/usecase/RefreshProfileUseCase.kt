package com.djyoo.smartnews.domain.usecase

import com.djyoo.smartnews.domain.policy.DecayPolicy
import com.djyoo.smartnews.domain.repository.UserProfileRepository

class RefreshProfileUseCase(
    private val userProfileRepository: UserProfileRepository,
    private val decayPolicy: DecayPolicy,
) {
    suspend operator fun invoke(
        keywordScores: Map<String, Double>,
        nowMillis: Long = System.currentTimeMillis(),
        threshold: Double = 0.05,
        topK: Int = 20,
    ) {
        TODO("Not implemented")
    }
}
