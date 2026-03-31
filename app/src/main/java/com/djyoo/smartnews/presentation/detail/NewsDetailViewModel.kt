package com.djyoo.smartnews.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.djyoo.smartnews.domain.usecase.RecordInteractionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel
    @Inject
    constructor(
        private val recordInteractionUseCase: RecordInteractionUseCase,
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val _state = MutableStateFlow(NewsDetailState.initial())
        val state: StateFlow<NewsDetailState> = _state.asStateFlow()
        private val startTime = System.currentTimeMillis()
        private val articleId: String = savedStateHandle.get<String>("articleId").orEmpty()

        fun processIntent(intent: NewsDetailIntent) {
        }

        private fun onExit() {
        }
    }
