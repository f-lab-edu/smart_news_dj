package com.djyoo.smartnews.domain.engine

import com.djyoo.smartnews.domain.matcher.KeywordMatcher
import com.djyoo.smartnews.domain.model.Article
import com.djyoo.smartnews.domain.model.UserKeyword
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class RecommendationEngineTest {
    private val matcher = KeywordMatcher()

    @Test
    fun `empty pool returns empty`() {
        val engine = RecommendationEngine(keywordMatcher = matcher, random = Random(0L))
        assertEquals(emptyList<Article>(), engine.recommend(emptyList(), emptyList()))
    }

    @Test
    fun `empty profile fills slots with deterministic random order`() {
        val articles = buildArticles(25, startId = 0)
        val engine = RecommendationEngine(keywordMatcher = matcher, random = Random(42L))
        val result = engine.recommend(articles, profile = emptyList())
        assertEquals(20, result.size)
        assertEquals(result.distinctBy { it.id }.size, result.size)
        result.forEach { a -> assertTrue(articles.any { it.id == a.id }) }
    }

    @Test
    fun `fewer than 20 articles returns all distinct`() {
        val articles = buildArticles(5, startId = 0)
        val engine = RecommendationEngine(keywordMatcher = matcher, random = Random(0L))
        val result = engine.recommend(articles, emptyList())
        assertEquals(5, result.size)
        assertEquals(articles.map { it.id }.toSet(), result.map { it.id }.toSet())
    }

    @Test
    fun `higher match score ranked before zero when profile present`() {
        val articles =
            listOf(
                article("a", keywords = listOf("테크")),
                article("b", keywords = listOf("스포츠")),
            )
        val profile =
            listOf(
                UserKeyword(keyword = "테크", score = 10.0, lastUpdated = 0L),
            )
        val engine = RecommendationEngine(keywordMatcher = matcher, random = Random(0L))
        val result = engine.recommend(articles, profile)
        assertEquals(2, result.size)
        assertEquals("a", result.first().id)
    }

    private fun buildArticles(
        count: Int,
        startId: Int,
    ): List<Article> =
        List(count) { index ->
            val id = startId + index
            article("article-$id", keywords = emptyList())
        }

    private fun article(
        id: String,
        keywords: List<String>,
    ): Article =
        Article(
            id = id,
            title = "t-$id",
            description = "d-$id",
            link = "https://e/$id",
            originalLink = "https://e/$id",
            pubDate = 0L,
            keywords = keywords,
            fetchedAt = 0L,
        )
}
