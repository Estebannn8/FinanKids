package com.universidad.finankids.data.sound

import android.content.Context
import android.media.MediaPlayer
import com.universidad.finankids.R

object MusicManager {

    private var mediaPlayer: MediaPlayer? = null
    var enabled = true

    fun start(context: Context) {
        if (!enabled) return

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.background).apply {  // Cambiar sonido background
                isLooping = true
            }
        }

        mediaPlayer?.start()
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
