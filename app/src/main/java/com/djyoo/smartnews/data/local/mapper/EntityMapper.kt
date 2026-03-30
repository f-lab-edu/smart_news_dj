package com.djyoo.smartnews.data.local.mapper

import com.djyoo.smartnews.data.local.entity.ArticleEntity
import com.djyoo.smartnews.data.local.entity.ArticleKeywordCrossRef
import com.djyoo.smartnews.data.local.entity.ArticleWithKeywords
import com.djyoo.smartnews.data.local.entity.InteractionEntity
import com.djyoo.smartnews.data.local.entity.UserKeywordEntity
import com.djyoo.smartnews.domain.model.Article
import com.djyoo.smartnews.domain.model.Interaction
import com.djyoo.smartnews.domain.model.UserKeyword

fun Article.toEntity(): ArticleEntity = TODO("Not implemented")

fun Article.toKeywordCrossRefs(): List<ArticleKeywordCrossRef> = TODO("Not implemented")

fun ArticleWithKeywords.toDomain(): Article = TODO("Not implemented")

fun UserKeywordEntity.toDomain(): UserKeyword = TODO("Not implemented")

fun UserKeyword.toEntity(): UserKeywordEntity = TODO("Not implemented")

fun Interaction.toEntity(): InteractionEntity = TODO("Not implemented")
