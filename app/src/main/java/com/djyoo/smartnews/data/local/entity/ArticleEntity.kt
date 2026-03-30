package com.djyoo.smartnews.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val link: String,
    val originalLink: String,
    val pubDate: Long,
    val fetchedAt: Long,
)
