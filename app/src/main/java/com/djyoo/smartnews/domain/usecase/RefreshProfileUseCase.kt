package com.djyoo.smartnews.domain.usecase

import com.djyoo.smartnews.domain.model.UserKeyword
import com.djyoo.smartnews.domain.policy.DecayPolicy
import com.djyoo.smartnews.domain.repository.UserProfileRepository

class RefreshProfileUseCase(
    private val userProfileRepository: UserProfileRepository,
    private val decayPolicy: DecayPolicy,
) {
    suspend operator fun invoke(keywordScores: Map<String, Double>) {
        if (keywordScores.isEmpty()) return
        val now = System.currentTimeMillis()
        val existing = userProfileRepository.getUserKeywordsSnapshot(TOP_K_KEYWORDS)
        val merged = mutableMapOf<String, UserKeyword>()
        for (userKeyword in existing) {
            val days = ((now - userKeyword.lastUpdated) / MS_PER_DAY).coerceAtLeast(0L)
            val decayed = decayPolicy.apply(userKeyword.score, days)
            merged[userKeyword.keyword] = UserKeyword(userKeyword.keyword, decayed, now)
        }

        for ((keyword, delta) in keywordScores) {
            val prev = merged[keyword]?.score ?: 0.0
            merged[keyword] = UserKeyword(keyword, prev + delta, now)
        }

        val filtered =
            merged.values
                .filter { it.score >= KEYWORD_SCORE_THRESHOLD }
                .sortedByDescending { it.score }
                .take(TOP_K_KEYWORDS)
        userProfileRepository.saveUserKeywords(filtered)
    }

    private companion object {
        const val KEYWORD_SCORE_THRESHOLD: Double = 0.05
        const val TOP_K_KEYWORDS: Int = 20
        const val MS_PER_DAY: Long = 86_400_000L
    }
}
