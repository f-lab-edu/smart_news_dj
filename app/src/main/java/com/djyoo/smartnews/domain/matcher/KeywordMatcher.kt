package com.djyoo.smartnews.domain.matcher

import com.djyoo.smartnews.domain.model.Article
import com.djyoo.smartnews.domain.model.UserKeyword

class KeywordMatcher {
    fun matchScore(
        article: Article,
        profile: List<UserKeyword>,
    ): Double {
        if (profile.isEmpty() || article.keywords.isEmpty()) return 0.0

        val profileByNorm =
            profile
                .mapNotNull { userKeyword ->
                    val key = normalizeKeyword(userKeyword.keyword)
                    if (key.isEmpty()) null else key to userKeyword.score
                }
                .groupBy(
                    { it.first },
                    { it.second },
                )
                .mapValues { (_, scores) ->
                    scores.sum()
                }

        return article.keywords
            .mapNotNull { articleKeyword ->
                val key = normalizeKeyword(articleKeyword)
                profileByNorm[key]
            }
            .sum()
    }

    private fun normalizeKeyword(raw: String): String =
        raw
            .trim()
            .lowercase()
}
