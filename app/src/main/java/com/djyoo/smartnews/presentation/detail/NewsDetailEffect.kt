package com.djyoo.smartnews.presentation.detail

sealed interface NewsDetailEffect {
    data object NavigateBack : NewsDetailEffect
}
