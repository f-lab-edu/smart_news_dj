package com.djyoo.smartnews.di

import com.djyoo.smartnews.domain.engine.RecommendationEngine
import com.djyoo.smartnews.domain.matcher.KeywordMatcher
import com.djyoo.smartnews.domain.policy.DecayPolicy
import com.djyoo.smartnews.domain.scorer.ScoreCalculator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideKeywordMatcher(): KeywordMatcher = KeywordMatcher()

    @Provides
    @Singleton
    fun provideRecommendationEngine(keywordMatcher: KeywordMatcher): RecommendationEngine = RecommendationEngine(keywordMatcher)

    @Provides
    @Singleton
    fun provideScoreCalculator(): ScoreCalculator = ScoreCalculator()

    @Provides
    @Singleton
    fun provideDecayPolicy(): DecayPolicy = DecayPolicy()
}
