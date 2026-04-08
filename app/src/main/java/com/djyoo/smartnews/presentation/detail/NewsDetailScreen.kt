package com.djyoo.smartnews.presentation.detail

import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.djyoo.smartnews.domain.model.Article
import com.djyoo.smartnews.presentation.theme.SmartNewsTheme
import kotlinx.coroutines.flow.collect

@Composable
fun NewsDetailScreen(
    onBack: () -> Unit,
    viewModel: NewsDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                NewsDetailEffect.NavigateBack -> onBack()
            }
        }
    }

    BackHandler {
        viewModel.processIntent(NewsDetailIntent.BackPressed)
    }

    NewsDetailContent(
        article = state.article,
        isLoading = state.isLoading,
        onScrollPercent = { percent -> viewModel.processIntent(NewsDetailIntent.UpdateScroll(percent)) },
        onBack = { viewModel.processIntent(NewsDetailIntent.BackPressed) },
    )
}

@Composable
fun NewsDetailContent(
    article: Article?,
    isLoading: Boolean,
    onScrollPercent: (Float) -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = "뒤로",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier =
                Modifier
                    .padding(16.dp)
                    .clickable(onClick = onBack),
        )

        when {
            isLoading -> {
                Box(
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
            article == null -> {
                Box(
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "기사를 불러올 수 없습니다.",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
            else -> {
                val url = article.originalLink.ifBlank { article.link }
                val isInPreview = LocalInspectionMode.current
                if (url.isBlank()) {
                    Box(
                        modifier =
                            Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "표시할 링크가 없습니다.",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                } else if (isInPreview) {
                    Box(
                        modifier =
                            Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "프리뷰: WebView 영역",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                } else {
                    key(article.id, url) {
                        AndroidView(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                            factory = { context ->
                                ScrollMetricsWebView(context).apply {
                                    settings.javaScriptEnabled = true
                                    settings.domStorageEnabled = true
                                    webViewClient = WebViewClient()
                                    setOnScrollChangeListener { v, _, _, _, _ ->
                                        val w = v as ScrollMetricsWebView
                                        onScrollPercent(w.verticalScrollProgress())
                                    }
                                    loadUrl(url)
                                }
                            },
                            onRelease = { it.destroy() },
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NewsDetailPreview() {
    SmartNewsTheme {
        NewsDetailContent(
            article =
                Article(
                    id = "1",
                    title = "샘플",
                    description = "",
                    link = "https://example.com",
                    originalLink = "https://example.com",
                    pubDate = 0L,
                    keywords = emptyList(),
                    fetchedAt = 0L,
                ),
            isLoading = false,
            onScrollPercent = {},
            onBack = {},
        )
    }
}
