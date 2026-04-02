package com.djyoo.smartnews.presentation.home

sealed interface HomeIntent {
    data object OnScreenEntered : HomeIntent

    data object OnRefreshRequested : HomeIntent

    data object OnReachedBottom : HomeIntent

    data class OnArticleClicked(
        val articleId: String,
    ) : HomeIntent
}
