package com.example.mobile_applications_hw.utilities

import android.content.Context
import android.media.MediaPlayer

class SingleSoundPlayer(context: Context) {

    private val appContext = context.applicationContext
    private var mediaPlayer: MediaPlayer? = null

    fun playSound(resId: Int) {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(appContext, resId)
            mediaPlayer?.apply {
                isLooping = false
                setVolume(1.0f, 1.0f)
                start()
                setOnCompletionListener {
                    it.release()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}