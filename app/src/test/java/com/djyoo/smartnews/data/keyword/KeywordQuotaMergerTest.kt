package com.djyoo.smartnews.data.keyword

import org.junit.Assert.assertEquals
import org.junit.Test

class KeywordQuotaMergerTest {
    @Test
    fun givenTitleTwoAndDescriptionThree_whenMerge_thenReturnsFiveInTitleFirstOrder() {
        val titleRanked = listOf("정치", "경제")
        val descriptionRanked = listOf("사회", "문화", "과학")
        val expected = listOf("정치", "경제", "사회", "문화", "과학")
        val actual =
            KeywordQuotaMerger.mergeTitleThenDescription(
                titleRankedDistinct = titleRanked,
                descriptionRankedDistinct = descriptionRanked,
            )
        assertEquals(expected, actual)
    }

    @Test
    fun givenOverlappingKeyword_whenMerge_thenDescriptionSkipsDuplicate() {
        val titleRanked = listOf("정치", "경제")
        val descriptionRanked = listOf("정치", "사회", "문화")
        val expected = listOf("정치", "경제", "사회", "문화")
        val actual =
            KeywordQuotaMerger.mergeTitleThenDescription(
                titleRankedDistinct = titleRanked,
                descriptionRankedDistinct = descriptionRanked,
            )
        assertEquals(expected, actual)
    }

    @Test
    fun givenEmptyTitle_whenMerge_thenFillsFromDescriptionOnlyUpToThree() {
        val titleRanked = emptyList<String>()
        val descriptionRanked = listOf("사회", "문화", "과학", "연예")
        val expected = listOf("사회", "문화", "과학")
        val actual =
            KeywordQuotaMerger.mergeTitleThenDescription(
                titleRankedDistinct = titleRanked,
                descriptionRankedDistinct = descriptionRanked,
            )
        assertEquals(expected, actual)
    }

    @Test
    fun givenMaxTotalZero_whenMerge_thenReturnsEmpty() {
        val expected = emptyList<String>()
        val actual =
            KeywordQuotaMerger.mergeTitleThenDescription(
                titleRankedDistinct = listOf("정치", "경제"),
                descriptionRankedDistinct = listOf("사회"),
                maxTotal = 0,
            )
        assertEquals(expected, actual)
    }

    @Test
    fun givenBlankEntries_whenMerge_thenIgnoresBlanksAndKeepsOrder() {
        val titleRanked = listOf("정치", "  ", "경제")
        val descriptionRanked = listOf("", "사회")
        val expected = listOf("정치", "경제", "사회")
        val actual =
            KeywordQuotaMerger.mergeTitleThenDescription(
                titleRankedDistinct = titleRanked,
                descriptionRankedDistinct = descriptionRanked,
            )
        assertEquals(expected, actual)
    }

    @Test
    fun givenMoreThanQuota_whenMerge_thenCapsAtFiveWithTitleTwoDescriptionThree() {
        val titleRanked = listOf("가", "나", "다")
        val descriptionRanked = listOf("라", "마", "바", "사")
        val expected = listOf("가", "나", "라", "마", "바")
        val actual =
            KeywordQuotaMerger.mergeTitleThenDescription(
                titleRankedDistinct = titleRanked,
                descriptionRankedDistinct = descriptionRanked,
            )
        assertEquals(expected, actual)
    }
}
