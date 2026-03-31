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
        private val getRecommendationsUseCase: GetRecommendationsUseCase, // todo 추후 사용 예정
    ) : ViewModel() {
        private val _state = MutableStateFlow(HomeState())
        val state: StateFlow<HomeState> = _state.asStateFlow()

        // Naver API uses 1-based start index.
        private var apiStartIndex = 1
        private val initialLoadSize = 100
        private val pageSize = 100
        private var isPaging = false

        init {
            processIntent(HomeIntent.LoadInitial)
        }

        fun processIntent(intent: HomeIntent) {
            when (intent) {
                HomeIntent.LoadInitial -> loadInitial()
                HomeIntent.Refresh -> refresh()
                HomeIntent.LoadMore -> loadMore()
                is HomeIntent.OpenArticle -> Unit
            }
        }

        private fun loadInitial() {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                runCatching {
                    fetchNewsUseCase(start = apiStartIndex, display = initialLoadSize)
                }.onSuccess { articles ->
                    _state.update { current -> current.copy(newsList = articles, isLoading = false) }
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
            val nextStart = apiStartIndex + _state.value.newsList.size
            viewModelScope.launch {
                runCatching {
                    fetchNewsPageUseCase(start = nextStart, display = pageSize)
                }.onSuccess { pageArticles ->
                    _state.update { current -> current.copy(newsList = appendUnique(current.newsList, pageArticles)) }
                }.onFailure {
                    // Keep current start for retry.
                }.also {
                    isPaging = false
                }
            }
        }

        fun refreshRecommendations() {
            // todo 추후 구현 예정
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
