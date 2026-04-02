package com.djyoo.smartnews.presentation.home

import com.djyoo.smartnews.domain.model.Article
import com.djyoo.smartnews.domain.usecase.FetchNewsPageUseCase
import com.djyoo.smartnews.domain.usecase.FetchNewsUseCase
import com.djyoo.smartnews.domain.usecase.GetRecommendationsUseCase
import com.djyoo.smartnews.test.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    // viewModelScope(Main)이 테스트 디스패처에서 동작하도록 보장한다.
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fetchNewsUseCase: FetchNewsUseCase = mockk()
    private val fetchNewsPageUseCase: FetchNewsPageUseCase = mockk()
    private val getRecommendationsUseCase: GetRecommendationsUseCase = mockk(relaxed = true)

    @Test
    fun `initial load fetches latest 100 articles`() =
        runTest {
            // 초기 진입 시 100개를 채우는지 검증한다.
            val expectedArticles = buildArticles(count = 100, startId = 0)
            coEvery { fetchNewsUseCase.invoke(any(), any(), any()) } returns expectedArticles
            coEvery { fetchNewsPageUseCase.invoke(any(), any(), any()) } returns emptyList()

            val viewModel = newViewModel()
            advanceUntilIdle()

            assertEquals(expectedArticles, viewModel.state.value.newsList)
            assertTrue(!viewModel.state.value.isLoading)
            coVerify(exactly = 1) { fetchNewsUseCase.invoke("뉴스", 1, 100) }
        }

    @Test
    fun `refresh resets paging and reloads first 100`() =
        runTest {
            // 새로고침 이후에는 start=1 기준으로 첫 페이지를 다시 조회해야 한다.
            val initialArticles = buildArticles(count = 100, startId = 0)
            val refreshedArticles = buildArticles(count = 100, startId = 1000)
            coEvery { fetchNewsUseCase.invoke(any(), any(), any()) } returnsMany listOf(initialArticles, refreshedArticles)
            coEvery { fetchNewsPageUseCase.invoke(any(), any(), any()) } returns emptyList()

            val viewModel = newViewModel()
            advanceUntilIdle()

            viewModel.processIntent(HomeIntent.OnRefreshRequested)
            advanceUntilIdle()

            assertEquals(refreshedArticles, viewModel.state.value.newsList)
            assertTrue(!viewModel.state.value.isLoading)
            coVerify(exactly = 2) { fetchNewsUseCase.invoke("뉴스", 1, 100) }
        }

    @Test
    fun `load more requests next api page and appends unique articles`() =
        runTest {
            // 일부 중복이 섞인 페이지를 주입해 appendUnique 동작을 검증한다.
            val initialArticles = buildArticles(count = 100, startId = 0)
            val pageArticles = buildArticles(count = 100, startId = 90) // 10 overlap + 90 new
            coEvery { fetchNewsUseCase.invoke(any(), any(), any()) } returns initialArticles
            coEvery { fetchNewsPageUseCase.invoke(any(), any(), any()) } returns pageArticles

            val viewModel = newViewModel()
            advanceUntilIdle()

            viewModel.processIntent(HomeIntent.OnReachedBottom)
            advanceUntilIdle()

            // expected = 초기 100개 + 중복 제거된 90개
            val expected =
                initialArticles +
                    pageArticles.filterNot { p -> initialArticles.any { it.id == p.id } }

            assertEquals(expected, viewModel.state.value.newsList)
            coVerify(exactly = 1) { fetchNewsPageUseCase.invoke("뉴스", 101, 100) }
        }

    private fun newViewModel(): HomeViewModel =
        HomeViewModel(
            fetchNewsPageUseCase = fetchNewsPageUseCase,
            fetchNewsUseCase = fetchNewsUseCase,
            getRecommendationsUseCase = getRecommendationsUseCase,
        )

    private fun buildArticles(
        count: Int,
        startId: Int,
    ): List<Article> =
        List(count) { index ->
            val id = startId + index
            Article(
                id = "article-$id",
                title = "title-$id",
                description = "description-$id",
                link = "https://example.com/$id",
                originalLink = "https://example.com/$id",
                pubDate = id.toLong(),
                keywords = emptyList(),
                fetchedAt = id.toLong(),
            )
        }
}
