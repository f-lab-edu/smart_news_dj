package com.djyoo.smartnews.data.repository

import com.djyoo.smartnews.data.local.dao.InteractionDao
import com.djyoo.smartnews.domain.model.Interaction
import com.djyoo.smartnews.domain.repository.InteractionRepository

class InteractionRepositoryImpl(
    private val interactionDao: InteractionDao,
) : InteractionRepository {
    override suspend fun insertInteraction(interaction: Interaction) {
        TODO("Not implemented")
    }
}
