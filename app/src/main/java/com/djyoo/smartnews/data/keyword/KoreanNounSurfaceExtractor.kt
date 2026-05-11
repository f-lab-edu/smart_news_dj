package com.djyoo.smartnews.data.keyword

import org.openkoreantext.processor.KoreanPosJava
import org.openkoreantext.processor.KoreanTokenJava
import org.openkoreantext.processor.OpenKoreanTextProcessorJava
import org.openkoreantext.processor.tokenizer.KoreanTokenizer.KoreanToken
import scala.collection.Seq

internal object KoreanNounSurfaceExtractor {
    fun nounSurfacesFromNormalizedKoreanText(normalizedText: CharSequence): List<String> {
        if (normalizedText.isBlank()) return emptyList()
        val tokenSequence = OpenKoreanTextProcessorJava.tokenize(normalizedText)

        @Suppress("UNCHECKED_CAST")
        val javaTokens =
            OpenKoreanTextProcessorJava.tokensToJavaKoreanTokenList(
                tokenSequence as Seq<KoreanToken>,
            ) as List<KoreanTokenJava>
        return javaTokens
            .asSequence()
            .filter { token ->
                token.pos == KoreanPosJava.Noun || token.pos == KoreanPosJava.ProperNoun
            }
            .map { it.text.trim() }
            .filter { it.isNotEmpty() }
            .toList()
    }
}
