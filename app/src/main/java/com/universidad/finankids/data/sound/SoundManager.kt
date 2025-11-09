package com.universidad.finankids.data.sound

import android.content.Context
import android.media.SoundPool

object SoundManager {

    private var soundPool: SoundPool? = null
    private val soundMap = mutableMapOf<AppSound, Int>()
    var enabled = true

    fun init(context: Context) {
        soundPool = SoundPool.Builder()
            .setMaxStreams(8)
            .build()

        AppSound.values().forEach { sound ->
            val soundId = soundPool!!.load(context, sound.resId, 1)
            soundMap[sound] = soundId
        }
    }

    fun play(sound: AppSound) {
        if (!enabled) return

        soundMap[sound]?.let {
            soundPool?.play(it, 1f, 1f, 1, 0, 1f)
        }
    }

    fun release() {
        soundPool?.release()
    }
}
