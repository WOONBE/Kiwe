package com.kiwe.kiosk.ui.screen.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import timber.log.Timber
import java.util.Locale

private const val TAG = "TextToSpeechManager"

class TextToSpeechManager(
    private val context: Context,
) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech = TextToSpeech(context, this)
    private var isInitialized = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.KOREAN
            isInitialized = true
        }
    }

    fun speak(text: String) {
        if (isInitialized && text.isNotEmpty()) {
            Timber.tag(TAG).d("speak: $text")
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    fun stop() {
        if (isInitialized) {
            tts.stop()
        }
    }

    fun shutdown() {
        tts.shutdown()
    }
}
