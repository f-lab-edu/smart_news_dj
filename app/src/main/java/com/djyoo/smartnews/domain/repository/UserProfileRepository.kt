package com.djyoo.smartnews.domain.repository

import com.djyoo.smartnews.domain.model.UserKeyword
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
    fun observeUserKeywords(limit: Int = 20): Flow<List<UserKeyword>>

    suspend fun getUserKeywordsSnapshot(limit: Int = 20): List<UserKeyword>

    suspend fun saveUserKeywords(keywords: List<UserKeyword>)
}
