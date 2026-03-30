package com.djyoo.smartnews.domain.model

data class Interaction(
    val articleId: String,
    val clicked: Boolean,
    val dwellTimeMs: Long,
    val scrollPercent: Float,
    val timestamp: Long,
)
