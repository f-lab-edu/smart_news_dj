package com.djyoo.smartnews.presentation.detail

import android.net.Uri
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
        private val articleId: String =
            try {
                Uri.decode(savedStateHandle.get<String>("articleId"))
            } catch (e: Exception) {
                savedStateHandle.get<String>("articleId").orEmpty() // 실패 시 원본 사용
            }

        fun processIntent(intent: NewsDetailIntent) {
        }

        private fun onExit() {
        }
    }
