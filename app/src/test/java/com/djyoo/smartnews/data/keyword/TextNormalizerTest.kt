package com.djyoo.smartnews.data.keyword

import org.junit.Assert.assertEquals
import org.junit.Test

class TextNormalizerTest {
    @Test
    fun givenLeadingAndTrailingWhitespace_whenNormalize_thenTrims() {
        val expected = "한국어 뉴스"
        val actual = TextNormalizer.normalizeForKeywordPipeline("  한국어 뉴스  ")
        assertEquals(expected, actual)
    }

    @Test
    fun givenRepeatedInternalWhitespace_whenNormalize_thenCollapsesToSingleSpace() {
        val expected = "제목 요약 본문"
        val actual = TextNormalizer.normalizeForKeywordPipeline("제목    요약\n\t본문")
        assertEquals(expected, actual)
    }

    @Test
    fun givenBlankInput_whenNormalize_thenReturnsEmpty() {
        val expected = ""
        val actual = TextNormalizer.normalizeForKeywordPipeline("   ")
        assertEquals(expected, actual)
    }

    @Test
    fun givenCompatibilityLigature_whenNormalize_thenNfkcExpandsToAsciiFi() {
        val expected = "fi"
        val actual = TextNormalizer.normalizeForKeywordPipeline("\uFB01")
        assertEquals(expected, actual)
    }
}
