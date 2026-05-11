package com.djyoo.smartnews.data.keyword

object KeywordQuotaMerger {
    fun mergeTitleThenDescription(
        titleRankedDistinct: List<String>,
        descriptionRankedDistinct: List<String>,
        maxTotal: Int = 5,
        maxFromTitle: Int = 2,
        maxFromDescription: Int = 3,
    ): List<String> {
        if (maxTotal <= 0) return emptyList()
        val merged = LinkedHashSet<String>()
        titleRankedDistinct
            .asSequence()
            .filter { it.isNotBlank() }
            .take(maxFromTitle)
            .forEach { merged.add(it) }
        descriptionRankedDistinct
            .asSequence()
            .filter { it.isNotBlank() }
            .filter { it !in merged }
            .take(maxFromDescription)
            .forEach { merged.add(it) }
        return merged.take(maxTotal).toList()
    }
}
