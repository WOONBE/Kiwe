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
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

private const val TAG = "STT"

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
                    RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH, // FreeForm 말고 websearch로 바꾸니까 응답속도 빨라짐
                )
                putExtra(
                    RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,
                    1000L,
                ) // 말 끝낸 킹능성 있을 때 대기할 1초
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
            if (speechRecognizer != null) {
                speechRecognizer?.destroy() // 기존 인스턴스 정리
            }
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
                }

                override fun onError(error: Int) {
                    isListening = false
                    Timber.tag(TAG).d("onError: $error")
                    handleError(error)
                }

                override fun onResults(results: Bundle?) {
                    isListening = false
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    matches?.let {
                        Timber.tag(TAG).d("onResults: $it")
                        listener?.onResultsReceived(it)
                    }
                    restartListeningImmediately() // 결과 후 즉시 다시 시작
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

        private fun restartListeningImmediately() {
            CoroutineScope(Dispatchers.Main).launch {
                speechRecognizer?.cancel() // 기존 인식 종료
                isListening = false
                startListening() // 바로 다시 시작
            }
        }

        private fun handleError(error: Int) {
            when (error) {
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> {
                    Timber.tag(TAG).d("Recognizer is busy. Retrying after delay...")
                    resetSpeechRecognizerWithDelay()
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

        fun stopListening() {
            if (!isListening) return
            isListening = false
            speechRecognizer?.cancel()
        }

        fun destroyRecognizer() {
            speechRecognizer?.destroy()
            speechRecognizer = null
            isListening = false
            Timber.tag(TAG).d("STT 파괴")
        }

        fun initializeRecognizer() {
            speechRecognizer =
                SpeechRecognizer.createSpeechRecognizer(context).apply {
                    setRecognitionListener(createRecognitionListener())
                }
            isListening = false
            Timber.tag(TAG).d("STT 인스턴스 초기화")
        }

        private fun restartListeningWithDelay(delayMillis: Long = delayTime) {
            CoroutineScope(Dispatchers.Main).launch {
                speechRecognizer?.cancel()
                isListening = false
                startListening()
            }
        }

        private fun resetSpeechRecognizerWithDelay() {
            CoroutineScope(Dispatchers.Main).launch {
                resetSpeechRecognizer()
                startListening()
            }
        }

        private fun resetSpeechRecognizer() {
            speechRecognizer?.destroy()
            initializeSpeechRecognizer()
            isListening = false
        }
    }

interface SpeechResultListener {
    fun onResultsReceived(results: List<String>)

    fun onPartialResultsReceived(partialResults: List<String>)
}
