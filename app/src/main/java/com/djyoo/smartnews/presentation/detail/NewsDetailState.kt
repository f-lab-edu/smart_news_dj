package com.djyoo.smartnews.presentation.detail

import com.djyoo.smartnews.domain.model.Article

data class NewsDetailState(
    /** 로드 완료 전에는 null (웹뷰/데이터 준비 전). */
    val article: Article?,
    /** 세션 동안 관측된 스크롤 깊이의 최댓값 (0f~1f). */
    val maxScrollPercent: Float,
) {
    companion object {
        fun initial(): NewsDetailState = NewsDetailState(article = null, maxScrollPercent = 0f)
    }
}
