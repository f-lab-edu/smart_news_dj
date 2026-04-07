package com.djyoo.smartnews.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.djyoo.smartnews.domain.model.Article
import com.djyoo.smartnews.domain.model.Interaction
import com.djyoo.smartnews.domain.usecase.LoadArticleDetailUseCase
import com.djyoo.smartnews.domain.usecase.RecordInteractionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel
    @Inject
    constructor(
        private val loadArticleDetailUseCase: LoadArticleDetailUseCase,
        private val recordInteractionUseCase: RecordInteractionUseCase,
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val _state = MutableStateFlow(NewsDetailState.initial())
        val state: StateFlow<NewsDetailState> = _state.asStateFlow()

        private val startTimeMs: Long = System.currentTimeMillis()

        private val articleId: String =
            decodeArg(savedStateHandle.get<String>("articleId").orEmpty())

        private val originalLinkArg: String =
            decodeArg(savedStateHandle.get<String>("originalLink").orEmpty())

        init {
            viewModelScope.launch {
                val loaded = loadArticleDetailUseCase(articleId)
                val article: Article? =
                    loaded
                        ?: originalLinkArg.takeIf { it.isNotBlank() }?.let { url ->
                            Article(
                                id = articleId,
                                title = "",
                                description = "",
                                link = url,
                                originalLink = url,
                                pubDate = 0L,
                                keywords = emptyList(),
                                fetchedAt = 0L,
                            )
                        }
                _state.update { it.copy(article = article, isLoading = false) }
            }
        }

        fun processIntent(intent: NewsDetailIntent) {
            when (intent) {
                is NewsDetailIntent.UpdateScroll -> {
                    _state.update { s ->
                        s.copy(maxScrollPercent = maxOf(s.maxScrollPercent, intent.percent.coerceIn(0f, 1f)))
                    }
                }
                is NewsDetailIntent.BackPressed -> {
                    handleBackPressed(intent.onDone)
                }
            }
        }

        private fun handleBackPressed(onDone: () -> Unit) {
            viewModelScope.launch {
                runCatching {
                    val article = state.value.article ?: return@runCatching
                    val dwellMs = (System.currentTimeMillis() - startTimeMs).coerceAtMost(DWELL_CAP_MS)
                    val interaction =
                        Interaction(
                            articleId = article.id,
                            clicked = true,
                            dwellTimeMs = dwellMs,
                            scrollPercent = state.value.maxScrollPercent,
                            timestamp = System.currentTimeMillis(),
                        )
                    recordInteractionUseCase(interaction)
                }
                onDone()
            }
        }

        private companion object {
            const val DWELL_CAP_MS: Long = 120_000L
        }

        private fun decodeArg(value: String): String =
            runCatching {
                URLDecoder.decode(value, StandardCharsets.UTF_8.name())
            }.getOrDefault(value)
    }
