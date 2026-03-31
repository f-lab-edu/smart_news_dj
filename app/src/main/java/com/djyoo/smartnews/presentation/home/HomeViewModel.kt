package com.djyoo.smartnews.presentation.home

import androidx.lifecycle.ViewModel
import com.djyoo.smartnews.domain.usecase.FetchNewsUseCase
import com.djyoo.smartnews.domain.usecase.GetRecommendationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val fetchNewsUseCase: FetchNewsUseCase,
        private val getRecommendationsUseCase: GetRecommendationsUseCase,
    ) : ViewModel() {
        private val _state = MutableStateFlow(HomeState())
        val state: StateFlow<HomeState> = _state.asStateFlow()

        private var start: Int = 1
        private val pageSize = 100

        fun processIntent(intent: HomeIntent) {
        }

        private fun observeArticles() {
        }

        private fun loadInitial() {
        }

        private fun refresh() {
        }

        private fun loadMore() {
        }

        fun refreshRecommendations() {
        }
    }
