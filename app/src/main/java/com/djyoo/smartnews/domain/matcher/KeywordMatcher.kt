package com.djyoo.smartnews.domain.matcher

import com.djyoo.smartnews.domain.model.Article
import com.djyoo.smartnews.domain.model.UserKeyword

class KeywordMatcher {
    fun matchScore(
        article: Article,
        profile: List<UserKeyword>,
    ): Double = TODO("Not implemented")
}
