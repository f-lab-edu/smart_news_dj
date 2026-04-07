package com.djyoo.smartnews.presentation.detail

import android.content.Context
import android.webkit.WebView

/**
 * [View]의 protected 스크롤 메서드는 외부 리스너에서 호출할 수 없어,
 * 서브클래스 내부에서 세로 스크롤 진행률(0f~1f)을 노출한다.
 */
class ScrollMetricsWebView(
    context: Context,
) : WebView(context) {
    fun verticalScrollProgress(): Float {
        val range = computeVerticalScrollRange() - computeVerticalScrollExtent()
        return if (range <= 0) {
            1f
        } else {
            (scrollY.toFloat() / range).coerceIn(0f, 1f)
        }
    }
}
