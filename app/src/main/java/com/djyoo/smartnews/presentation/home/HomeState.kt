package com.djyoo.smartnews.presentation.home

import com.djyoo.smartnews.domain.model.Article

data class HomeState(
    val recommendations: List<Article> = emptyList(),
    val newsList: List<Article> = emptyList(),
    val isLoading: Boolean = false,
)
