package com.djyoo.smartnews.presentation.detail

sealed interface NewsDetailIntent {
    /** WebView 세로 스크롤 진행도 (0f~1f). ViewModel에서 maxScrollPercent와 max 병합. */
    data class UpdateScroll(
        val percent: Float,
    ) : NewsDetailIntent

    /** 사용자가 상세 화면에서 뒤로가기를 요청했다. */
    data class BackPressed(
        val onDone: () -> Unit,
    ) : NewsDetailIntent
}
