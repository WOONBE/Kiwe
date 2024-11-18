package com.kiwe.kiosk.ui.screen.utils

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject

@Suppress("DEPRECATION")
class TextToSpeechManager
    @Inject
    constructor(
        @ApplicationContext
        private val context: Context,
    ) : TextToSpeech.OnInitListener {
        private var tts: TextToSpeech = TextToSpeech(context, this)
        private var isInitialized = false
        private var onComplete: (() -> Unit)? = null
        private var onStart: (() -> Unit)? = null

        override fun onInit(status: Int) {
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale.KOREAN
                isInitialized = true

                tts.setOnUtteranceProgressListener(
                    object : UtteranceProgressListener() {
                        override fun onStart(utteranceId: String?) {
                            Handler(Looper.getMainLooper()).post {
                                onStart?.invoke()
                            }
                        }

                        override fun onDone(utteranceId: String?) {
                            Handler(Looper.getMainLooper()).post {
                                onComplete?.invoke()
                            }
                        }

                        override fun onError(utteranceId: String?) {
                            // 필요 시 에러 처리
                        }
                    },
                )
            }
        }

        fun setOnCompleteListener(onComplete: () -> Unit) {
            this.onComplete = onComplete
        }

        fun setOnStartListener(onStart: () -> Unit) {
            this.onStart = onStart
        }

        fun speak(text: String) {
            if (isInitialized && text.isNotEmpty()) {
                val bundle = Bundle()
                bundle.putInt(TextToSpeech.Engine.KEY_PARAM_STREAM, AudioManager.STREAM_MUSIC)
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, bundle, "TTS_ID")
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
