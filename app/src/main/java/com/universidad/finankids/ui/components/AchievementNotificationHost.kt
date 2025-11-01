package com.universidad.finankids.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.universidad.finankids.viewmodel.AchievementsViewModel

@Composable
fun AchievementNotificationHost(
    achievementsViewModel: AchievementsViewModel
) {
    val state by achievementsViewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 50.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            state.notifications.forEach { notif ->

                AchievementNotificationCard(
                    notif = notif,
                    onDismiss = {
                        achievementsViewModel.removeNotification(notif)
                    }
                )
            }
        }
    }
}
