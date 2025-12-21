package com.example.alkalimah.utils

import android.content.Context
import android.media.MediaPlayer
import java.io.File

class AudioPlayer(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    fun playAudio(blob: ByteArray?) {
        blob?.let {
            val tempFile = File.createTempFile("word_audio", "mp3", context.cacheDir)
            tempFile.writeBytes(it)

            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(tempFile.absolutePath)
                prepare()
                start()
            }
        }
    }
}
