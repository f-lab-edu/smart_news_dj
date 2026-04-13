package com.djyoo.smartnews.presentation.detail

import androidx.lifecycle.SavedStateHandle
import com.djyoo.smartnews.domain.model.Article
import com.djyoo.smartnews.domain.model.Interaction
import com.djyoo.smartnews.domain.usecase.LoadArticleDetailUseCase
import com.djyoo.smartnews.domain.usecase.RecordInteractionUseCase
import com.djyoo.smartnews.test.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsDetailViewModelTest {
    // viewModelScope(Main)이 테스트 디스패처에서 동작하도록 보장한다.
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val loadArticleDetailUseCase: LoadArticleDetailUseCase = mockk()
    private val recordInteractionUseCase: RecordInteractionUseCase = mockk()

    @Test
    fun `init loads article and turns off loading`() =
        runTest {
            // 상세 진입 시 기사 로드 성공 케이스를 검증한다.
            val expected = buildArticle(id = "article-1", originalLink = "https://example.com/1")
            coEvery { loadArticleDetailUseCase.invoke("article-1") } returns expected
            coEvery { recordInteractionUseCase.invoke(any()) } returns Unit

            val viewModel = newViewModel(articleId = "article-1", originalLink = "https://fallback.example")
            advanceUntilIdle()

            assertEquals(expected, viewModel.state.value.article)
            assertFalse(viewModel.state.value.isLoading)
        }

    @Test
    fun `init creates fallback article when repository returns null`() =
        runTest {
            // DB 조회 실패 시 originalLink 인자로 폴백 기사를 구성하는지 검증한다.
            coEvery { loadArticleDetailUseCase.invoke("article-fallback") } returns null
            coEvery { recordInteractionUseCase.invoke(any()) } returns Unit

            val fallbackUrl = "https://fallback.example"
            val viewModel = newViewModel(articleId = "article-fallback", originalLink = fallbackUrl)
            advanceUntilIdle()

            val expected =
                Article(
                    id = "article-fallback",
                    title = "",
                    description = "",
                    link = fallbackUrl,
                    originalLink = fallbackUrl,
                    pubDate = 0L,
                    keywords = emptyList(),
                    fetchedAt = 0L,
                )
            assertEquals(expected, viewModel.state.value.article)
            assertFalse(viewModel.state.value.isLoading)
        }

    @Test
    fun `init keeps article null when no source exists`() =
        runTest {
            // DB도 없고 originalLink도 비어 있으면 null 상태를 유지해야 한다.
            coEvery { loadArticleDetailUseCase.invoke("") } returns null
            coEvery { recordInteractionUseCase.invoke(any()) } returns Unit

            val viewModel = newViewModel(articleId = "", originalLink = "")
            advanceUntilIdle()

            assertNull(viewModel.state.value.article)
            assertFalse(viewModel.state.value.isLoading)
        }

    @Test
    fun `updateScroll keeps max value only`() =
        runTest {
            // 스크롤 퍼센트는 최신값이 아니라 세션 최대값을 유지해야 한다.
            coEvery { loadArticleDetailUseCase.invoke("article-scroll") } returns
                buildArticle("article-scroll", "https://example.com/scroll")
            coEvery { recordInteractionUseCase.invoke(any()) } returns Unit

            val viewModel = newViewModel(articleId = "article-scroll", originalLink = "https://example.com/scroll")
            advanceUntilIdle()

            viewModel.processIntent(NewsDetailIntent.UpdateScroll(0.2f))
            viewModel.processIntent(NewsDetailIntent.UpdateScroll(0.8f))
            viewModel.processIntent(NewsDetailIntent.UpdateScroll(0.4f))

            assertEquals(0.8f, viewModel.state.value.maxScrollPercent, 0.0001f)
        }

    @Test
    fun `backPressed intent records interaction and emits navigateBack once`() =
        runTest {
            // 사용자의 뒤로 의도 전달 시 상호작용 기록 + NavigateBack effect를 검증한다.
            coEvery { loadArticleDetailUseCase.invoke("article-exit") } returns buildArticle("article-exit", "https://example.com/exit")
            val interactionSlot = slot<Interaction>()
            coEvery { recordInteractionUseCase.invoke(capture(interactionSlot)) } returns Unit

            val viewModel = newViewModel(articleId = "article-exit", originalLink = "https://example.com/exit")
            advanceUntilIdle()
            viewModel.processIntent(NewsDetailIntent.UpdateScroll(0.85f))
            val effectDeferred = async { viewModel.effects.first() }

            viewModel.processIntent(NewsDetailIntent.BackPressed)
            advanceUntilIdle()

            assertEquals(NewsDetailEffect.NavigateBack, effectDeferred.await())
            coVerify(exactly = 1) { recordInteractionUseCase.invoke(any()) }
            assertEquals("article-exit", interactionSlot.captured.articleId)
            assertTrue(interactionSlot.captured.clicked)
            assertEquals(0.85f, interactionSlot.captured.scrollPercent, 0.0001f)
            assertTrue(interactionSlot.captured.dwellTimeMs in 0L..120_000L)
            assertNotNull(interactionSlot.captured.timestamp)
        }

    @Test
    fun `backPressed intent can run repeatedly without crash`() =
        runTest {
            // 사용자가 연속으로 뒤로를 눌러도 예외 없이 기록이 누적되어야 한다.
            coEvery { loadArticleDetailUseCase.invoke("article-repeat") } returns
                buildArticle("article-repeat", "https://example.com/repeat")
            coEvery { recordInteractionUseCase.invoke(any()) } returns Unit

            val viewModel = newViewModel(articleId = "article-repeat", originalLink = "https://example.com/repeat")
            advanceUntilIdle()

            repeat(5) { viewModel.processIntent(NewsDetailIntent.BackPressed) }
            advanceUntilIdle()

            coVerify(exactly = 5) { recordInteractionUseCase.invoke(any()) }
        }

    private fun newViewModel(
        articleId: String,
        originalLink: String,
    ): NewsDetailViewModel =
        NewsDetailViewModel(
            loadArticleDetailUseCase = loadArticleDetailUseCase,
            recordInteractionUseCase = recordInteractionUseCase,
            savedStateHandle =
                SavedStateHandle(
                    mapOf(
                        "articleId" to articleId,
                        "originalLink" to originalLink,
                    ),
                ),
        )

    private fun buildArticle(
        id: String,
        originalLink: String,
    ): Article =
        Article(
            id = id,
            title = "title-$id",
            description = "description-$id",
            link = originalLink,
            originalLink = originalLink,
            pubDate = 1L,
            keywords = emptyList(),
            fetchedAt = 1L,
        )
}
