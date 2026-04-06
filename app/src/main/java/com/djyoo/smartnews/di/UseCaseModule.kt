package com.djyoo.smartnews.di

import com.djyoo.smartnews.domain.engine.RecommendationEngine
import com.djyoo.smartnews.domain.policy.DecayPolicy
import com.djyoo.smartnews.domain.repository.ArticleRepository
import com.djyoo.smartnews.domain.repository.InteractionRepository
import com.djyoo.smartnews.domain.repository.UserProfileRepository
import com.djyoo.smartnews.domain.scorer.ScoreCalculator
import com.djyoo.smartnews.domain.usecase.FetchNewsPageUseCase
import com.djyoo.smartnews.domain.usecase.FetchNewsUseCase
import com.djyoo.smartnews.domain.usecase.GetRecommendationsUseCase
import com.djyoo.smartnews.domain.usecase.RecordInteractionUseCase
import com.djyoo.smartnews.domain.usecase.RefreshProfileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideFetchNewsUseCase(articleRepository: ArticleRepository): FetchNewsUseCase = FetchNewsUseCase(articleRepository)

    @Provides
    @Singleton
    fun provideFetchNewsPageUseCase(articleRepository: ArticleRepository): FetchNewsPageUseCase = FetchNewsPageUseCase(articleRepository)

    @Provides
    @Singleton
    fun provideGetRecommendationsUseCase(
        articleRepository: ArticleRepository,
        userProfileRepository: UserProfileRepository,
        recommendationEngine: RecommendationEngine,
    ): GetRecommendationsUseCase = GetRecommendationsUseCase(articleRepository, userProfileRepository, recommendationEngine)

    @Provides
    @Singleton
    fun provideRefreshProfileUseCase(
        userProfileRepository: UserProfileRepository,
        decayPolicy: DecayPolicy,
    ): RefreshProfileUseCase = RefreshProfileUseCase(userProfileRepository, decayPolicy)

    @Provides
    @Singleton
    fun provideRecordInteractionUseCase(
        interactionRepository: InteractionRepository,
        articleRepository: ArticleRepository,
        scoreCalculator: ScoreCalculator,
        refreshProfileUseCase: RefreshProfileUseCase,
    ): RecordInteractionUseCase = RecordInteractionUseCase(interactionRepository, articleRepository, scoreCalculator, refreshProfileUseCase)
}
