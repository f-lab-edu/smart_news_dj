package com.djyoo.smartnews.domain.engine

import com.djyoo.smartnews.domain.matcher.KeywordMatcher
import com.djyoo.smartnews.domain.model.Article
import com.djyoo.smartnews.domain.model.UserKeyword
import kotlin.math.min
import kotlin.math.round
import kotlin.random.Random

class RecommendationEngine(
    private val keywordMatcher: KeywordMatcher,
    private val random: Random = Random.Default,
) {
    fun recommend(
        articles: List<Article>,
        profile: List<UserKeyword>,
    ): List<Article> {
        val pool = articles.distinctBy { it.id }
        if (pool.isEmpty()) return emptyList()

        val slotTotal = min(TOTAL_COUNT, pool.size)
        val personalizedSlots = round(slotTotal * PERSONALIZED_RATIO).toInt().coerceAtLeast(0)
        val explorationSlots = (slotTotal - personalizedSlots).coerceAtLeast(0)

        val scored = pool.map { article -> article to keywordMatcher.matchScore(article, profile) }
        val selected = LinkedHashMap<String, Article>()

        fillPersonalizedSlots(scored, personalizedSlots, selected)
        fillExplorationSlots(pool, explorationSlots, selected)

        return selected.values.toList()
    }

    private fun fillPersonalizedSlots(
        scored: List<Pair<Article, Double>>,
        personalizedSlots: Int,
        selected: LinkedHashMap<String, Article>,
    ) {
        if (personalizedSlots <= 0) return

        val positive = scored.filter { it.second > 0.0 }.sortedByDescending { it.second }
        for ((article, _) in positive) {
            if (selected.size >= personalizedSlots) break
            selected.putIfAbsent(article.id, article)
        }

        val remainingSlots = personalizedSlots - selected.size
        if (remainingSlots <= 0) return

        val candidates = scored.map { it.first }.filter { it.id !in selected.keys }
        pickRandomDistinct(candidates, remainingSlots, selected)
    }

    private fun fillExplorationSlots(
        pool: List<Article>,
        explorationSlots: Int,
        selected: LinkedHashMap<String, Article>,
    ) {
        if (explorationSlots <= 0) return
        val candidates = pool.filter { it.id !in selected.keys }
        pickRandomDistinct(candidates, explorationSlots, selected)
    }

    private fun pickRandomDistinct(
        candidates: List<Article>,
        count: Int,
        selected: LinkedHashMap<String, Article>,
    ) {
        if (count <= 0 || candidates.isEmpty()) return
        val take = min(count, candidates.size)
        val shuffled = candidates.shuffled(random)
        var added = 0
        for (article in shuffled) {
            if (added >= take) break
            if (selected.putIfAbsent(article.id, article) == null) {
                added++
            }
        }
    }

    private companion object {
        const val TOTAL_COUNT = 20
        const val PERSONALIZED_RATIO = 0.7
    }
}
