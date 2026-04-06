package com.djyoo.smartnews.di

import android.content.Context
import androidx.room.Room
import com.djyoo.smartnews.data.local.dao.ArticleDao
import com.djyoo.smartnews.data.local.dao.InteractionDao
import com.djyoo.smartnews.data.local.dao.UserKeywordDao
import com.djyoo.smartnews.data.local.db.SmartNewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): SmartNewsDatabase =
        Room
            .databaseBuilder(
                context,
                SmartNewsDatabase::class.java,
                "smartnews.db",
            ).build()

    @Provides
    fun provideArticleDao(database: SmartNewsDatabase): ArticleDao = database.articleDao()

    @Provides
    fun provideUserKeywordDao(database: SmartNewsDatabase): UserKeywordDao = database.userKeywordDao()

    @Provides
    fun provideInteractionDao(database: SmartNewsDatabase): InteractionDao = database.interactionDao()
}
