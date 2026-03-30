package com.djyoo.smartnews.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.djyoo.smartnews.data.local.dao.ArticleDao
import com.djyoo.smartnews.data.local.dao.InteractionDao
import com.djyoo.smartnews.data.local.dao.UserKeywordDao
import com.djyoo.smartnews.data.local.entity.ArticleEntity
import com.djyoo.smartnews.data.local.entity.ArticleKeywordCrossRef
import com.djyoo.smartnews.data.local.entity.InteractionEntity
import com.djyoo.smartnews.data.local.entity.UserKeywordEntity

@Database(
    entities = [
        ArticleEntity::class,
        ArticleKeywordCrossRef::class,
        UserKeywordEntity::class,
        InteractionEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class SmartNewsDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao

    abstract fun userKeywordDao(): UserKeywordDao

    abstract fun interactionDao(): InteractionDao
}
