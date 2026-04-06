package com.djyoo.smartnews.di

import com.djyoo.smartnews.data.keyword.OktKeywordExtractor
import com.djyoo.smartnews.data.local.dao.ArticleDao
import com.djyoo.smartnews.data.local.dao.InteractionDao
import com.djyoo.smartnews.data.local.dao.UserKeywordDao
import com.djyoo.smartnews.data.remote.api.NaverNewsApi
import com.djyoo.smartnews.data.repository.ArticleRepositoryImpl
import com.djyoo.smartnews.data.repository.InteractionRepositoryImpl
import com.djyoo.smartnews.data.repository.UserProfileRepositoryImpl
import com.djyoo.smartnews.domain.keyword.KeywordExtractor
import com.djyoo.smartnews.domain.repository.ArticleRepository
import com.djyoo.smartnews.domain.repository.InteractionRepository
import com.djyoo.smartnews.domain.repository.UserProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideKeywordExtractor(): KeywordExtractor = OktKeywordExtractor()

    @Provides
    @Singleton
    fun provideArticleRepository(
        articleDao: ArticleDao,
        naverNewsApi: NaverNewsApi,
        keywordExtractor: KeywordExtractor,
    ): ArticleRepository = ArticleRepositoryImpl(articleDao, naverNewsApi, keywordExtractor)

    @Provides
    @Singleton
    fun provideUserProfileRepository(userKeywordDao: UserKeywordDao): UserProfileRepository = UserProfileRepositoryImpl(userKeywordDao)

    @Provides
    @Singleton
    fun provideInteractionRepository(interactionDao: InteractionDao): InteractionRepository = InteractionRepositoryImpl(interactionDao)
}
