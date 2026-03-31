package com.djyoo.smartnews.domain.model

data class UserKeyword(
    val keyword: String,
    val score: Double,
    val lastUpdated: Long,
)
