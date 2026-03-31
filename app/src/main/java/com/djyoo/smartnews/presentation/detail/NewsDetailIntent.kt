package com.djyoo.smartnews.presentation.detail

sealed interface NewsDetailIntent {
    data class UpdateScroll(
        val percent: Float,
    ) : NewsDetailIntent

    data object OnExit : NewsDetailIntent
}
