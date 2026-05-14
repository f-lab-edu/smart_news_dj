package com.djyoo.smartnews.data.keyword

import com.djyoo.smartnews.domain.keyword.KeywordExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openkoreantext.processor.OpenKoreanTextProcessorJava

/**
 * Open Korean Text로 명사 후보를 얻고, 불용어·길이 등 휴리스틱과 제목/설명 쿼터로 최종 키워드를 고른다.
 */
class OktKeywordExtractor : KeywordExtractor {
    override suspend fun extract(
        title: String,
        description: String,
    ): List<String> =
        withContext(Dispatchers.Default) {
            val normalizedTitle = normalizeThenOpenKoreanNormalize(title)
            val normalizedDescription = normalizeThenOpenKoreanNormalize(description)
            val titleNouns =
                KoreanNounSurfaceExtractor.nounSurfacesFromNormalizedKoreanText(normalizedTitle)
            val descriptionNouns =
                KoreanNounSurfaceExtractor.nounSurfacesFromNormalizedKoreanText(normalizedDescription)
            val titleRanked = rankedDistinctSurfacesPassingHeuristic(titleNouns)
            val descriptionRanked = rankedDistinctSurfacesPassingHeuristic(descriptionNouns)
            KeywordQuotaMerger.mergeTitleThenDescription(
                titleRankedDistinct = titleRanked,
                descriptionRankedDistinct = descriptionRanked,
                maxTotal = KeywordQuotaMerger.DEFAULT_MAX_TOTAL,
                maxFromTitle = KeywordQuotaMerger.DEFAULT_MAX_FROM_TITLE,
                maxFromDescription = KeywordQuotaMerger.DEFAULT_MAX_FROM_DESCRIPTION,
            )
        }

    private fun normalizeThenOpenKoreanNormalize(raw: String): CharSequence {
        val pipelineNormalized = TextNormalizer.normalizeForKeywordPipeline(raw)
        return OpenKoreanTextProcessorJava.normalize(pipelineNormalized)
    }

    private fun rankedDistinctSurfacesPassingHeuristic(surfaces: List<String>): List<String> {
        val passing = surfaces.filter { surface -> passesKeywordHeuristic(surface) }
        val firstOccurrenceIndex =
            passing.withIndex().groupBy { it.value }.mapValues { entry -> entry.value.minOf { it.index } }
        val frequencyBySurface = passing.groupingBy { it }.eachCount()
        return passing
            .distinct()
            .sortedWith(
                compareByDescending<String> { frequencyBySurface[it] ?: 0 }
                    .thenBy { firstOccurrenceIndex[it] ?: 0 },
            )
    }

    private fun passesKeywordHeuristic(surface: String): Boolean =
        when {
            surface.length < MIN_KEYWORD_LENGTH -> false
            surface in Stopwords.knownIrrelevantSurfaces -> false
            surface.all { it.isDigit() } -> false
            surface.startsWith("http", ignoreCase = true) -> false
            else -> true
        }

    private companion object {
        const val MIN_KEYWORD_LENGTH: Int = 2
    }
}
