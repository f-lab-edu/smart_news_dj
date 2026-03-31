package com.djyoo.smartnews.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.djyoo.smartnews.R
import com.djyoo.smartnews.domain.model.Article
import com.djyoo.smartnews.presentation.theme.SmartNewsTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@Composable
fun HomeScreen(
    onOpenDetail: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreenContent(
        state = state,
        onOpenDetail = onOpenDetail,
        onRefresh = { viewModel.processIntent(HomeIntent.Refresh) },
        onLoadMore = { viewModel.processIntent(HomeIntent.LoadMore) },
    )
}

@Composable
internal fun HomeScreenContent(
    state: HomeState,
    onOpenDetail: (String) -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
) {
    val listState = rememberLazyListState()
    val totalItemCount = state.recommendations.size + state.newsList.size + 2 // section headers

    LaunchedEffect(listState, totalItemCount) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo
                .lastOrNull()
                ?.index ?: -1
        }.map { lastVisibleIndex -> lastVisibleIndex >= totalItemCount - 1 && totalItemCount > 0 }
            .distinctUntilChanged()
            .filter { it }
            .collect { onLoadMore() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(id = R.string.recommend_news_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = "새로고침",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable(onClick = onRefresh),
                    )
                }
            }
            items(state.recommendations, key = { "rec_${it.id}" }) { article ->
                ArticleListItem(
                    article = article,
                    onClick = { onOpenDetail(article.id) },
                    modifier = Modifier.padding(horizontal = 12.dp),
                )
            }
            item {
                Text(
                    text = stringResource(id = R.string.all_news_title),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
            items(state.newsList, key = { "all_${it.id}" }) { article ->
                ArticleListItem(
                    article = article,
                    onClick = { onOpenDetail(article.id) },
                    modifier = Modifier.padding(horizontal = 12.dp),
                )
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}

@Composable
private fun ArticleListItem(
    article: Article,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
            )
            Text(
                text = article.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
            )
        }
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
        HomeScreenContent(
            state = state,
            onOpenDetail = {},
            onRefresh = {},
            onLoadMore = {},
        )
    }
}
