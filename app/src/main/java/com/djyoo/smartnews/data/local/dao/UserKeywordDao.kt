package com.djyoo.smartnews.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.djyoo.smartnews.data.local.entity.UserKeywordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserKeywordDao {
    @Query("SELECT * FROM user_keywords ORDER BY score DESC LIMIT :limit")
    fun observeTopKeywords(limit: Int): Flow<List<UserKeywordEntity>>

    @Query("SELECT * FROM user_keywords ORDER BY score DESC LIMIT :limit")
    suspend fun getTopKeywords(limit: Int): List<UserKeywordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(keywords: List<UserKeywordEntity>)

    @Query("DELETE FROM user_keywords")
    suspend fun clearAll()
}
