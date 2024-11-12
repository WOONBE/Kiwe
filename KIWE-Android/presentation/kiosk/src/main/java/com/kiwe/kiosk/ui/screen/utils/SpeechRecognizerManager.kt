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
        private val delayTime = 200L
        private var speechRecognizer: SpeechRecognizer? = null
        private val recognizerIntent: Intent =
            Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    putExtra(RecognizerIntent.EXTRA_MASK_OFFENSIVE_WORDS, true)
                }
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
                )
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREAN)
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            }

        private var isListening = false
        private var listener: SpeechResultListener? = null

        fun setSpeechResultListener(listener: SpeechResultListener) {
            this.listener = listener
        }

        init {
            initializeSpeechRecognizer()
        }

        private fun initializeSpeechRecognizer() {
            speechRecognizer =
                SpeechRecognizer.createSpeechRecognizer(context).apply {
                    setRecognitionListener(createRecognitionListener())
                }
        }

        private fun createRecognitionListener(): RecognitionListener =
            object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    Timber.tag(TAG).d("onReadyForSpeech")
                }

                override fun onBeginningOfSpeech() {
                    Timber.tag(TAG).d("onBeginningOfSpeech")
//                    listener?.onSpeechStarted()
                }

                override fun onRmsChanged(rmsdB: Float) {
//                    Timber.tag(TAG).d("onRmsChanged: $rmsdB")
                }

                override fun onBufferReceived(buffer: ByteArray?) {
                    Timber.tag(TAG).d("onBufferReceived")
                }

                override fun onEndOfSpeech() {
                    Timber.tag(TAG).d("onEndOfSpeech")
//                    listener?.onSpeechEnded()
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
                    val partial =
                        partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
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
                    Timber.tag(TAG).d("Recognizer is busy. Retrying after delay...")
                    resetSpeechRecognizerWithDelay(delayTime) // 1초 후 재설정
                }

                SpeechRecognizer.ERROR_CLIENT -> {
                    Timber.tag(TAG).d("Client error. Resetting SpeechRecognizer.")
                    resetSpeechRecognizer()
                    startListening()
                }

                else -> {
                    Timber.tag(TAG).d("Unhandled error: $error. Restarting...")
                    restartListeningWithDelay()
                }
            }
        }

        fun startListening() {
            if (isListening) return // Early Return 패턴 적용
            isListening = true
            speechRecognizer?.startListening(recognizerIntent)
        }

        private fun restartListeningWithDelay(delayMillis: Long = delayTime) {
            CoroutineScope(Dispatchers.Main).launch {
                delay(delayMillis)
                if (isListening) {
                    speechRecognizer?.stopListening()
                    startListening()
                }
            }
        }

        private fun resetSpeechRecognizerWithDelay(delayMillis: Long) {
            CoroutineScope(Dispatchers.Main).launch {
                delay(delayMillis)
                resetSpeechRecognizer()
                startListening()
            }
        }

        private fun resetSpeechRecognizer() {
            speechRecognizer?.destroy()
            initializeSpeechRecognizer()
            isListening = false
        }

        fun stopListening() {
            isListening = false
            speechRecognizer?.stopListening()
        }
    }

interface SpeechResultListener {
    fun onResultsReceived(results: List<String>)

    fun onPartialResultsReceived(partialResults: List<String>)
//    fun onSpeechStarted()
//    fun onSpeechEnded()
}
