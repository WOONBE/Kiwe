package com.kiwe.kiosk.ui.screen.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

private const val TAG = "SpeechRecognizerManager"

class SpeechRecognizerManager
    @Inject
    constructor(
        @ApplicationContext
        private val context: Context,
    ) {
        private var speechRecognizer: SpeechRecognizer =
            SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(createRecognitionListener())
            }
        private val recognizerIntent: Intent =
            Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    putExtra(RecognizerIntent.EXTRA_MASK_OFFENSIVE_WORDS, true)
                }
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREAN)
            }

        private var listener: SpeechResultListener? = null

        fun setSpeechResultListener(listener: SpeechResultListener) {
            this.listener = listener
        }

        private fun createRecognitionListener(): RecognitionListener =
            object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    Timber.tag(TAG).d("onReadyForSpeech")
                }

                override fun onBeginningOfSpeech() {
                    Timber.tag(TAG).d("onBeginningOfSpeech")
                }

                override fun onRmsChanged(rmsdB: Float) {
                    // Timber.tag(TAG).d("onRmsChanged: $rmsdB")
                }

                override fun onBufferReceived(buffer: ByteArray?) {
                    Timber.tag(TAG).d("onBufferReceived")
                }

                override fun onEndOfSpeech() {
                    Timber.tag(TAG).d("onEndOfSpeech")
                }

                override fun onError(error: Int) {
                    Timber.tag(TAG).d("onError: $error")
                    handleError(error)
                }

                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    matches?.let {
                        Timber.tag(TAG).d("onResults: $it")
                        listener?.onResultsReceived(it)
                    }
                    restartListeningWithDelay()
                }

                override fun onPartialResults(partialResults: Bundle?) {
                    val partial = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    partial?.let {
                        Timber.tag(TAG).d("onPartialResults: $it")
                        listener?.onPartialResultsReceived(it)
                    }
                }

                override fun onEvent(
                    eventType: Int,
                    params: Bundle?,
                ) {
                    Timber.tag(TAG).d("onEvent")
                }
            }

        private fun handleError(error: Int) {
            when (error) {
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> {
                    Timber.tag(TAG).d("Recognizer is busy. Retrying...")
                    restartListeningWithDelay()
                }
                SpeechRecognizer.ERROR_CLIENT -> {
                    Timber.tag(TAG).d("Client error. Resetting SpeechRecognizer.")
                    resetSpeechRecognizer()
                    startListening()
                }
                else -> {
                    Timber.tag(TAG).d("Unhandled error: $error")
                    restartListeningWithDelay()
                }
            }
        }

        fun startListening() {
            Timber.tag(TAG).d("startListening")
            speechRecognizer.startListening(recognizerIntent)
        }

        private fun restartListeningWithDelay() {
            CoroutineScope(Dispatchers.Main).launch {
                delay(500L) // 0.1s
                speechRecognizer.stopListening()
                startListening()
            }
        }

        fun stopListening() {
            speechRecognizer.stopListening()
        }

        private fun resetSpeechRecognizer() {
            speechRecognizer.destroy()
            speechRecognizer =
                SpeechRecognizer.createSpeechRecognizer(context).apply {
                    setRecognitionListener(createRecognitionListener())
                }
        }
    }

interface SpeechResultListener {
    fun onResultsReceived(results: List<String>)

    fun onPartialResultsReceived(partialResults: List<String>)
}
