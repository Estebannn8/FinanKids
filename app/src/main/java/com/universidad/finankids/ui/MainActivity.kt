package com.universidad.finankids.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.universidad.finankids.data.model.JsonUploader
import com.universidad.finankids.data.sound.MusicManager
import com.universidad.finankids.data.sound.SoundManager
import com.universidad.finankids.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar SoundManager y MusicManager
        SoundManager.init(this)
        MusicManager.start(this)

        // JsonUploader.cargarYSubirLecciones(this)
        enableEdgeToEdge()
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                AppNavigation(modifier = Modifier.padding(innerPadding))
            }
        }
    }

    override fun onPause() {
        super.onPause()
        MusicManager.pause()
    }

    override fun onResume() {
        super.onResume()
        if (MusicManager.enabled) {
            MusicManager.start(this)
        }
    }

}
