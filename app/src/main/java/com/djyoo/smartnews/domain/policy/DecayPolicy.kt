package com.djyoo.smartnews.domain.policy

import kotlin.math.pow

class DecayPolicy(
    private val base: Double = 0.9,
) {
    fun apply(
        score: Double,
        days: Long,
    ): Double = score * base.pow(days.toDouble())
}
