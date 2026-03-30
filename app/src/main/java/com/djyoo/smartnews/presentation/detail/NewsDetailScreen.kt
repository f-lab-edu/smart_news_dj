package com.djyoo.smartnews.presentation.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.djyoo.smartnews.domain.model.Article
import com.djyoo.smartnews.presentation.theme.SmartNewsTheme

@Composable
fun NewsDetailScreen(
    onBack: () -> Unit,
    viewModel: NewsDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NewsDetailContent(
        article = state.article,
        onBack = onBack,
        onScrollChange = { _, _ -> },
        onExit = { },
    )
}

@Composable
fun NewsDetailContent(
    article: Article?,
    onBack: () -> Unit,
    onScrollChange: (Int, Int) -> Unit = { _, _ -> },
    onExit: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(scrollState.value, scrollState.maxValue) {
    }

    BackHandler {
        onBack()
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(text = article?.title ?: "", style = MaterialTheme.typography.titleLarge)
        Text(text = article?.description ?: "")
        Button(onClick = {
            onBack()
        }) {
            Text("뒤로 가기")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewsDetailPreview() {
    SmartNewsTheme {
        NewsDetailContent(
            article =
                Article(
                    id = "1",
                    title = "샘플 뉴스 제목",
                    description = "이것은 샘플 뉴스 기사의 내용입니다. 여러 줄의 텍스트가 표시될 수 있도록 길게 작성되었습니다. 스마트 뉴스 앱의 상세 화면 미리보기를 위한 데이터입니다.",
                    link = "https://example.com",
                    originalLink = "https://example.com",
                    pubDate = System.currentTimeMillis(),
                    keywords = listOf("샘플", "뉴스", "안드로이드"),
                    fetchedAt = System.currentTimeMillis(),
                ),
            onBack = {},
        )
    }
}
