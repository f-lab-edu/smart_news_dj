package com.djyoo.smartnews.presentation.home

sealed interface HomeIntent {
    data object LoadInitial : HomeIntent

    data object Refresh : HomeIntent

    data object LoadMore : HomeIntent

    data class OpenArticle(
        val articleId: String,
    ) : HomeIntent
}
