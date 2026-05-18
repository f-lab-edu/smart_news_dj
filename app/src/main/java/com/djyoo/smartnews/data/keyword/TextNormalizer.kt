package com.djyoo.smartnews.data.keyword

import java.text.Normalizer

object TextNormalizer {
    fun normalizeForKeywordPipeline(raw: String): String {
        val trimmed = raw.trim()
        if (trimmed.isEmpty()) return ""
        val compatibilityNormalized = Normalizer.normalize(trimmed, Normalizer.Form.NFKC)
        return compatibilityNormalized.replace(Regex("\\s+"), " ")
    }
}
