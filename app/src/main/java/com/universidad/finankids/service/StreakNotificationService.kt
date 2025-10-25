package com.universidad.finankids.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.universidad.finankids.R

/*
class StreakNotificationService(private val context: Context) {

    fun sendStreakReminder(currentStreak: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Crear canal de notificación (para Android 8+)
        val channel = NotificationChannel(
            "streak_channel",
            "Recordatorios de Racha",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, "streak_channel")
            .setContentTitle("¡No pierdas tu racha!")
            .setContentText("Tienes una racha de $currentStreak días. Completa una lección hoy para mantenerla.")
            .setSmallIcon(R.drawable.ic_racha_normal)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }

    fun sendStreakLostNotification() {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            "streak_channel",
            "Recordatorios de Racha",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, "streak_channel")
            .setContentTitle("Racha perdida 😔")
            .setContentText("Tu racha se ha reiniciado. ¡Vuelve a empezar hoy!")
            .setSmallIcon(R.drawable.ic_racha_apagada)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(2, notification)
    }
}
 */