package com.djyoo.smartnews.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ArticleWithKeywords(
    @Embedded val article: ArticleEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "articleId",
    )
    val keywords: List<ArticleKeywordCrossRef>,
)
