package com.djyoo.smartnews.domain.engine

import com.djyoo.smartnews.domain.matcher.KeywordMatcher
import com.djyoo.smartnews.domain.model.Article
import com.djyoo.smartnews.domain.model.UserKeyword
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.min
import kotlin.math.round
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
        val expected = expectedWhenProfileEmpty(articles = articles, random = Random(42L))
        assertEquals(expected, result)
    }

    @Test
    fun `fewer than 20 articles returns all distinct`() {
        val articles = buildArticles(5, startId = 0)
        val engine = RecommendationEngine(keywordMatcher = matcher, random = Random(0L))
        val result = engine.recommend(articles, emptyList())
        val expected = expectedWhenProfileEmpty(articles = articles, random = Random(0L))
        assertEquals(expected, result)
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
        val expected = listOf(articles[0], articles[1])
        assertEquals(expected, result)
    }

    private fun expectedWhenProfileEmpty(
        articles: List<Article>,
        random: Random,
    ): List<Article> {
        val pool = articles.distinctBy { it.id }
        val slotTotal = min(20, pool.size)
        val personalizedSlots = round(slotTotal * 0.7).toInt().coerceAtLeast(0)
        val explorationSlots = (slotTotal - personalizedSlots).coerceAtLeast(0)
        val selected = mutableMapOf<String, Article>()

        val personalizedCandidates = pool.shuffled(random).take(personalizedSlots)
        personalizedCandidates.forEach { article -> selected.putIfAbsent(article.id, article) }

        val explorationCandidates =
            pool
                .filterNot { it.id in selected }
                .shuffled(random)
                .take(explorationSlots)
        explorationCandidates.forEach { article -> selected.putIfAbsent(article.id, article) }

        return selected.values.toList()
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
