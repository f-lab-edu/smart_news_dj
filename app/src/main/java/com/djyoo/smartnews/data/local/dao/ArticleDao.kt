package com.djyoo.smartnews.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.djyoo.smartnews.data.local.entity.ArticleEntity
import com.djyoo.smartnews.data.local.entity.ArticleKeywordCrossRef
import com.djyoo.smartnews.data.local.entity.ArticleWithKeywords
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Transaction
    @Query("SELECT * FROM articles ORDER BY pubDate DESC LIMIT :limit")
    fun observeArticlesWithKeywords(limit: Int): Flow<List<ArticleWithKeywords>>

    @Transaction
    @Query("SELECT * FROM articles ORDER BY pubDate DESC LIMIT :limit")
    suspend fun getArticlesWithKeywords(limit: Int): List<ArticleWithKeywords>

    @Transaction
    @Query("SELECT * FROM articles WHERE id = :id LIMIT 1")
    suspend fun getArticleWithKeywordsById(id: String): ArticleWithKeywords?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertArticles(articles: List<ArticleEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertArticleKeywords(crossRefs: List<ArticleKeywordCrossRef>)

    @Query(
        """
        DELETE FROM articles
        WHERE id IN (
            SELECT id FROM articles
            ORDER BY pubDate DESC
            LIMIT -1 OFFSET :maxCount
        )
        """,
    )
    suspend fun trimToMaxCount(maxCount: Int)
}
