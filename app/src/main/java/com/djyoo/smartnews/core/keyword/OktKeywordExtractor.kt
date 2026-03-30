package com.djyoo.smartnews.core.keyword

import com.djyoo.smartnews.domain.keyword.KeywordExtractor

class OktKeywordExtractor : KeywordExtractor {
    override suspend fun extract(
        title: String,
        description: String,
    ): List<String> = TODO("Not implemented")
}
