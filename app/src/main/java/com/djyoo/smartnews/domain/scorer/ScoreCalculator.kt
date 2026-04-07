package com.djyoo.smartnews.domain.scorer

import com.djyoo.smartnews.domain.model.Interaction

class ScoreCalculator(
    private val clickWeight: Double = 1.0,
    private val dwellWeight: Double = 1.0,
    private val scrollWeight: Double = 1.0,
    private val dwellCapMs: Long = 120_000L,
) {
    fun calculate(interaction: Interaction): Double {
        val clickScore = if (interaction.clicked) clickWeight else 0.0
        val dwellNorm = (interaction.dwellTimeMs.coerceAtMost(dwellCapMs).toDouble() / dwellCapMs)
        val dwellScore = dwellNorm * dwellWeight
        val scrollScore =
            if (interaction.scrollPercent >= SCROLL_MEANINGFUL_THRESHOLD) {
                scrollWeight
            } else {
                interaction.scrollPercent.coerceIn(0f, 1f).toDouble() * scrollWeight
            }
        return clickScore + dwellScore + scrollScore
    }

    private companion object {
        private const val SCROLL_MEANINGFUL_THRESHOLD = 0.7f
    }
}
