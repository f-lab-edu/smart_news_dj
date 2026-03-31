package com.djyoo.smartnews.core.keyword

import com.djyoo.smartnews.domain.keyword.KeywordExtractor

/**
 * Okt(Open Korean Text)는 한국어 형태소 분석기입니다.(lib)
 * 제목/설명 텍스트에서 명사 등 키워드 후보를 추출하는 데 사용합니다.
 */
class OktKeywordExtractor : KeywordExtractor {
    override suspend fun extract(
        title: String,
        description: String,
    ): List<String> = TODO("Not implemented")
}
