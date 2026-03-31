package com.djyoo.smartnews.domain.engine

import com.djyoo.smartnews.domain.matcher.KeywordMatcher
import com.djyoo.smartnews.domain.model.Article
import com.djyoo.smartnews.domain.model.UserKeyword

class RecommendationEngine(
    private val keywordMatcher: KeywordMatcher,
) {
    fun recommend(
        articles: List<Article>,
        profile: List<UserKeyword>,
        totalCount: Int = 20,
        personalizedRatio: Double = 0.7,
    ): List<Article> = TODO("Not implemented")
}
