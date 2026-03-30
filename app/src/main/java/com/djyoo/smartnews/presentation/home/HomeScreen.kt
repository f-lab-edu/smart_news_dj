package com.djyoo.smartnews.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.djyoo.smartnews.R
import com.djyoo.smartnews.domain.model.Article
import com.djyoo.smartnews.presentation.theme.SmartNewsTheme

@Composable
fun HomeScreen(
    onOpenDetail: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        state = state,
        onOpenDetail = onOpenDetail,
        onLoadMore = { viewModel.processIntent(HomeIntent.LoadMore) }
    )
}

@Composable
private fun HomeScreen(
    state: HomeState,
    onOpenDetail: (String) -> Unit,
    onLoadMore: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(id = R.string.recommend_news_title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = stringResource(id = R.string.all_news_title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    val sampleArticles =
        List(5) { i ->
            Article(
                id = i.toString(),
                title = "Article Title $i",
                description = "This is the description for article $i.",
                link = "",
                originalLink = "",
                pubDate = 0,
                keywords = listOf("Keyword"),
                fetchedAt = 0,
            )
        }
    val state =
        HomeState(
            recommendations = sampleArticles.take(3),
            newsList = sampleArticles,
            isLoading = false,
        )
    SmartNewsTheme {
        HomeScreen(
            state = state,
            onOpenDetail = {},
            onLoadMore = {},
        )
    }
}
