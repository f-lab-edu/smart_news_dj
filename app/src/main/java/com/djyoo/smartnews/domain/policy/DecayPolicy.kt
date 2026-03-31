package com.djyoo.smartnews.domain.policy

class DecayPolicy(
    private val base: Double = 0.9,
) {
    fun apply(
        score: Double,
        days: Long,
    ): Double = TODO("Not implemented")
}
