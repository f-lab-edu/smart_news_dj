package com.djyoo.smartnews.data.keyword

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

private data class KeywordExtractionContract(
    val atMostFiveKeywords: Boolean,
    val eachKeywordAtLeastTwoCharacters: Boolean,
    val keywordsArePairwiseDistinct: Boolean,
    val containsNoPureDigitToken: Boolean,
    val containsNoHttpPrefixToken: Boolean,
) {
    companion object {
        fun from(keywords: List<String>): KeywordExtractionContract =
            KeywordExtractionContract(
                atMostFiveKeywords = keywords.size <= 5,
                eachKeywordAtLeastTwoCharacters = keywords.all { it.length >= 2 },
                keywordsArePairwiseDistinct = keywords.distinct().size == keywords.size,
                containsNoPureDigitToken = keywords.none { token -> token.all { it.isDigit() } },
                containsNoHttpPrefixToken =
                    keywords.none { token -> token.startsWith("http", ignoreCase = true) },
            )
    }
}

class OktKeywordExtractorTest {
    private val extractor = OktKeywordExtractor()

    @Test
    fun givenEmptyTitleAndDescription_whenExtract_thenReturnsEmptyList() =
        runTest {
            val expected = emptyList<String>()
            val actual = extractor.extract(title = "", description = "")
            assertEquals(expected, actual)
        }

    @Test
    fun givenWhitespaceOnlyTitleAndDescription_whenExtract_thenReturnsEmptyList() =
        runTest {
            val expected = emptyList<String>()
            val actual = extractor.extract(title = "   \n\t  ", description = "  ")
            assertEquals(expected, actual)
        }

    @Test
    fun givenKoreanTitleAndDescription_whenExtract_thenSatisfiesPipelineContract() =
        runTest {
            val actual =
                extractor.extract(
                    title = "정부가 경제 정책을 발표했다",
                    description = "금융 시장과 산업계 반응이 주목된다. 관련 보도가 이어진다.",
                )
            val expectedContract =
                KeywordExtractionContract(
                    atMostFiveKeywords = true,
                    eachKeywordAtLeastTwoCharacters = true,
                    keywordsArePairwiseDistinct = true,
                    containsNoPureDigitToken = true,
                    containsNoHttpPrefixToken = true,
                )
            assertEquals(expectedContract, KeywordExtractionContract.from(actual))
        }
}
