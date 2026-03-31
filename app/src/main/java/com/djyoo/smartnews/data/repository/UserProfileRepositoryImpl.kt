package com.djyoo.smartnews.data.repository

import com.djyoo.smartnews.data.local.dao.UserKeywordDao
import com.djyoo.smartnews.domain.model.UserKeyword
import com.djyoo.smartnews.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class UserProfileRepositoryImpl(
    private val userKeywordDao: UserKeywordDao,
) : UserProfileRepository {
    override fun observeUserKeywords(limit: Int): Flow<List<UserKeyword>> = flowOf(emptyList())

    override suspend fun getUserKeywordsSnapshot(limit: Int): List<UserKeyword> = TODO("Not implemented")

    override suspend fun saveUserKeywords(keywords: List<UserKeyword>) {
        TODO("Not implemented")
    }
}
