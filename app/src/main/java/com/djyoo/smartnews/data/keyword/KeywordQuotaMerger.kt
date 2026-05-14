package com.djyoo.smartnews.data.keyword

object KeywordQuotaMerger {
    const val DEFAULT_MAX_TOTAL: Int = 5
    const val DEFAULT_MAX_FROM_TITLE: Int = 2
    const val DEFAULT_MAX_FROM_DESCRIPTION: Int = 3

    fun mergeTitleThenDescription(
        titleRankedDistinct: List<String>,
        descriptionRankedDistinct: List<String>,
        maxTotal: Int,
        maxFromTitle: Int,
        maxFromDescription: Int,
    ): List<String> {
        if (maxTotal <= 0) return emptyList()
        val merged = mutableSetOf<String>()
        titleRankedDistinct
            .asSequence()
            .filter(String::isNotBlank)
            .take(maxFromTitle)
            .forEach(merged::add)
        descriptionRankedDistinct
            .asSequence()
            .filter(String::isNotBlank)
            .filter { it !in merged }
            .take(maxFromDescription)
            .forEach(merged::add)
        return merged.take(maxTotal).toList()
    }
}
