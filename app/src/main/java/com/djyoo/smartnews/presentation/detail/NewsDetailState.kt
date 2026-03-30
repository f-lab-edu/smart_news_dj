package com.djyoo.smartnews.presentation.detail

import com.djyoo.smartnews.domain.model.Article

data class NewsDetailState(
    val article: Article? = null,
    val scrollPercent: Float = 0f,
)
