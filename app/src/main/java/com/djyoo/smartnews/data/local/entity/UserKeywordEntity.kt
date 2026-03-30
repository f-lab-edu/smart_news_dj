package com.djyoo.smartnews.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_keywords")
data class UserKeywordEntity(
    @PrimaryKey val keyword: String,
    val score: Double,
    val lastUpdated: Long,
)
