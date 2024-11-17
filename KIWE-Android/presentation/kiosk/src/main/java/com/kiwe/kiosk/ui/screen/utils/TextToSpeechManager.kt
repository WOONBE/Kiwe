package com.kiwe.kiosk.ui.screen.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject

private const val TAG = "TextToSpeechManager"

class TextToSpeechManager
    @Inject
    constructor(
        @ApplicationContext
        private val context: Context,
    ) : TextToSpeech.OnInitListener {
        private var tts: TextToSpeech = TextToSpeech(context, this)
        private var isInitialized = false
        private var onComplete: (() -> Unit)? = null

        override fun onInit(status: Int) {
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale.KOREAN
                isInitialized = true
            }
        }

        fun setOnCompleteListener(onComplete: () -> Unit) {
            this.onComplete = onComplete
        }

        fun speak(text: String) {
            if (isInitialized && text.isNotEmpty()) {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "TTS_ID")
                tts.setOnUtteranceCompletedListener { onComplete?.invoke() }
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
