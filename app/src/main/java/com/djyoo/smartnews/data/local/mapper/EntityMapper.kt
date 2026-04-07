package com.djyoo.smartnews.data.local.mapper

import com.djyoo.smartnews.data.local.entity.ArticleEntity
import com.djyoo.smartnews.data.local.entity.ArticleKeywordCrossRef
import com.djyoo.smartnews.data.local.entity.ArticleWithKeywords
import com.djyoo.smartnews.data.local.entity.InteractionEntity
import com.djyoo.smartnews.data.local.entity.UserKeywordEntity
import com.djyoo.smartnews.domain.model.Article
import com.djyoo.smartnews.domain.model.Interaction
import com.djyoo.smartnews.domain.model.UserKeyword

fun Article.toEntity(): ArticleEntity =
    ArticleEntity(
        id = id,
        title = title,
        description = description,
        link = link,
        originalLink = originalLink,
        pubDate = pubDate,
        fetchedAt = fetchedAt,
    )

fun Article.toKeywordCrossRefs(): List<ArticleKeywordCrossRef> =
    keywords
        .distinct()
        .map { keyword ->
            ArticleKeywordCrossRef(articleId = id, keyword = keyword, source = "unknown")
        }

fun ArticleWithKeywords.toDomain(): Article =
    Article(
        id = article.id,
        title = article.title,
        description = article.description,
        link = article.link,
        originalLink = article.originalLink,
        pubDate = article.pubDate,
        keywords = keywords.map { it.keyword },
        fetchedAt = article.fetchedAt,
    )

fun UserKeywordEntity.toDomain(): UserKeyword =
    UserKeyword(
        keyword = keyword,
        score = score,
        lastUpdated = lastUpdated,
    )

fun UserKeyword.toEntity(): UserKeywordEntity =
    UserKeywordEntity(
        keyword = keyword,
        score = score,
        lastUpdated = lastUpdated,
    )

fun Interaction.toEntity(): InteractionEntity =
    InteractionEntity(
        articleId = articleId,
        clicked = clicked,
        dwellTimeMs = dwellTimeMs,
        scrollPercent = scrollPercent,
        timestamp = timestamp,
    )
