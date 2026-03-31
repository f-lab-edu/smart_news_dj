package com.djyoo.smartnews.domain.scorer

import com.djyoo.smartnews.domain.model.Interaction

class ScoreCalculator(
    private val clickWeight: Double = 1.0,
    private val dwellWeight: Double = 1.0,
    private val scrollWeight: Double = 1.0,
    private val dwellCapMs: Long = 120_000L,
) {
    fun calculate(interaction: Interaction): Double = TODO("Not implemented")
}
