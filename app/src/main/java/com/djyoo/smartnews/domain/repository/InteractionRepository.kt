package com.djyoo.smartnews.domain.repository

import com.djyoo.smartnews.domain.model.Interaction

interface InteractionRepository {
    suspend fun insertInteraction(interaction: Interaction)
}
