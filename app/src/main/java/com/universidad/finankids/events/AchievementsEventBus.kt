package com.universidad.finankids.events

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object AchievementsEventBus {

    private val channel = Channel<AchievementTrigger>(capacity = Channel.BUFFERED)

    val events = channel.receiveAsFlow()

    suspend fun emit(event: AchievementTrigger) {
        channel.send(event)
    }
}