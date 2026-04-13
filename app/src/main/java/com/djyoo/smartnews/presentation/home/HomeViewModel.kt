package com.djyoo.smartnews.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.djyoo.smartnews.domain.model.Article
import com.djyoo.smartnews.domain.usecase.FetchNewsPageUseCase
import com.djyoo.smartnews.domain.usecase.FetchNewsUseCase
import com.djyoo.smartnews.domain.usecase.GetRecommendationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val fetchNewsPageUseCase: FetchNewsPageUseCase,
        private val fetchNewsUseCase: FetchNewsUseCase,
        private val getRecommendationsUseCase: GetRecommendationsUseCase,
    ) : ViewModel() {
        private companion object {
            const val DEFAULT_QUERY: String = "뉴스"
        }

        private val _state = MutableStateFlow(HomeState())
        val state: StateFlow<HomeState> = _state.asStateFlow()

        /** 추천 제외 전 누적 기사(첫 로드 DB 스냅샷 + 페이징). */
        private var newsPoolUnfiltered: List<Article> = emptyList()

        private var apiStartIndex = 1
        private val initialLoadSize = 100
        private val pageSize = 100
        private var isPaging = false

        init {
            processIntent(HomeIntent.OnScreenEntered)
        }

        fun processIntent(intent: HomeIntent) {
            when (intent) {
                HomeIntent.OnScreenEntered -> loadInitial()
                HomeIntent.OnRefreshRequested -> refresh()
                HomeIntent.OnReachedBottom -> loadMore()
                is HomeIntent.OnArticleClicked -> Unit
            }
        }

        private fun loadInitial() {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                runCatching {
                    fetchNewsUseCase(query = DEFAULT_QUERY, start = apiStartIndex, display = initialLoadSize)
                }.onSuccess { articles ->
                    newsPoolUnfiltered = articles
                    val recommendations = runCatching { getRecommendationsUseCase() }.getOrDefault(emptyList())
                    applyRecommendationsAndNewsList(recommendations, articles)
                    _state.update { it.copy(isLoading = false) }
                }.onFailure {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }

        private fun refresh() {
            apiStartIndex = 1
            isPaging = false
            loadInitial()
        }

        private fun loadMore() {
            if (isPaging) return
            isPaging = true
            val nextStart = apiStartIndex + newsPoolUnfiltered.size
            val recIds =
                _state.value.recommendations
                    .map { it.id }
                    .toHashSet()
            viewModelScope.launch {
                runCatching {
                    fetchNewsPageUseCase(query = DEFAULT_QUERY, start = nextStart, display = pageSize)
                }.onSuccess { pageArticles ->
                    newsPoolUnfiltered = appendUnique(newsPoolUnfiltered, pageArticles)
                    _state.update { current ->
                        current.copy(
                            newsList = newsPoolUnfiltered.filterNot { it.id in recIds },
                        )
                    }
                }.onFailure {
                    // Keep current start for retry.
                }.also {
                    isPaging = false
                }
            }
        }

        fun refreshRecommendations() {
            viewModelScope.launch {
                runCatching { getRecommendationsUseCase() }.onSuccess { recommendations ->
                    applyRecommendationsAndNewsList(recommendations, newsPoolUnfiltered)
                }
            }
        }

        private fun applyRecommendationsAndNewsList(
            recommendations: List<Article>,
            pool: List<Article>,
        ) {
            val recIds = recommendations.map { it.id }.toHashSet()
            _state.update { current ->
                current.copy(
                    recommendations = recommendations,
                    newsList = pool.filterNot { it.id in recIds },
                )
            }
        }

        private fun appendUnique(
            current: List<Article>,
            incoming: List<Article>,
        ): List<Article> {
            val existingIds = current.asSequence().map { it.id }.toHashSet()
            val newOnes = incoming.filterNot { it.id in existingIds }
            return current + newOnes
        }
    }
