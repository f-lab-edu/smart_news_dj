package com.djyoo.smartnews.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "article_keywords",
    primaryKeys = ["articleId", "keyword"],
    foreignKeys = [
        ForeignKey(
            entity = ArticleEntity::class,
            parentColumns = ["id"],
            childColumns = ["articleId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["articleId"]), Index(value = ["keyword"])],
)
data class ArticleKeywordCrossRef(
    val articleId: String,
    val keyword: String,
    val source: String,
)
