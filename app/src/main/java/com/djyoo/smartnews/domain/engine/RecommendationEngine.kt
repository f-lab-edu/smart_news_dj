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
    ): List<Article> = TODO("Not implemented")

    private companion object {
        const val TOTAL_COUNT = 20
        const val PERSONALIZED_RATIO = 0.7
    }
}
