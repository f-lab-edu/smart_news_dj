package com.djyoo.smartnews.data.repository

import com.djyoo.smartnews.data.local.dao.UserKeywordDao
import com.djyoo.smartnews.data.local.mapper.toDomain
import com.djyoo.smartnews.data.local.mapper.toEntity
import com.djyoo.smartnews.domain.model.UserKeyword
import com.djyoo.smartnews.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserProfileRepositoryImpl(
    private val userKeywordDao: UserKeywordDao,
) : UserProfileRepository {
    override fun observeUserKeywords(limit: Int): Flow<List<UserKeyword>> =
        userKeywordDao.observeTopKeywords(limit).map { list -> list.map { it.toDomain() } }

    override suspend fun getUserKeywordsSnapshot(limit: Int): List<UserKeyword> = userKeywordDao.getTopKeywords(limit).map { it.toDomain() }

    override suspend fun saveUserKeywords(keywords: List<UserKeyword>) {
        userKeywordDao.upsertAll(keywords.map { it.toEntity() })
    }
}
