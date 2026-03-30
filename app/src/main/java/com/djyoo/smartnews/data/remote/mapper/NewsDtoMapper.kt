package com.djyoo.smartnews.data.remote.mapper

import com.djyoo.smartnews.data.remote.dto.NaverNewsItemDto
import com.djyoo.smartnews.domain.model.Article

fun NaverNewsItemDto.toDomain(
    keywords: List<String>,
    fetchedAt: Long,
): Article = TODO("Not implemented")
