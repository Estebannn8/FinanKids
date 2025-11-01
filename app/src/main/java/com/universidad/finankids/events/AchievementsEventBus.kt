package com.universidad.finankids.events

import com.universidad.finankids.viewmodel.AchievementsViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object AchievementsEventBus {

    private val channel = Channel<AchievementsViewModel.AchievementTrigger>(capacity = Channel.BUFFERED)

    val events = channel.receiveAsFlow()

    suspend fun emit(event: AchievementsViewModel.AchievementTrigger) {
        channel.send(event)
    }
}