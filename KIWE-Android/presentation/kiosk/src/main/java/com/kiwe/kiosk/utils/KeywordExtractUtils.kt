package com.kiwe.kiosk.utils

import android.util.Log

enum class Sentiment {
    POSITIVE, NEGATIVE, UNKNOWN
}

enum class OrderType {
    PACKAGING, IN_STORE, UNKNOWN
}
private const val TAG = "키워드 기반 추출"
object KeywordExtractUtils {
    private val packagingKeywords = setOf("포장", "테이크", "가져", "들고", "싸주세")
    private val inStoreKeywords = setOf("매장", "여기서", "먹고")
    private val positiveKeywords = setOf("예", "네", "맞아요", "그래", "좋아", "응", "어")
    private val negativeKeywords = setOf("아니", "아니요", "아냐", "싫어", "아닌데")

    fun isPackagingIntent(input: String): Boolean {
        val lowerCaseInput = input.lowercase()
        return packagingKeywords.any { it in lowerCaseInput }
    }

    fun isInStoreIntent(input: String): Boolean {
        val lowerCaseInput = input.lowercase()
        return inStoreKeywords.any { it in lowerCaseInput }
    }

    fun positiveIntent(input: String): Boolean {
        val lowerCaseInput = input.lowercase()
        return positiveKeywords.any { it in lowerCaseInput }
    }

    fun negativeIntent(input: String): Boolean {
        val lowerCaseInput = input.lowercase()
        return negativeKeywords.any { it in lowerCaseInput }
    }

    fun detectSentiment(input: String): Sentiment {
        return when {
            positiveIntent(input) -> Sentiment.POSITIVE
            negativeIntent(input) -> Sentiment.NEGATIVE
            else -> Sentiment.UNKNOWN
        }
    }

    fun detectOrderType(input: String): OrderType {
        return when {
            isPackagingIntent(input) -> OrderType.PACKAGING
            isInStoreIntent(input) -> OrderType.IN_STORE
            else -> OrderType.UNKNOWN
        }
    }

    fun exampleForHereOrToGo() {
        val userInput = "커피 한 잔 포장해주세요"
        val extractResult = detectOrderType(userInput)
        when (extractResult) {
            OrderType.PACKAGING ->  Log.d(TAG, "포장 요청 주문입니다.")
            OrderType.IN_STORE ->  Log.d(TAG, "매장 요청 주문입니다.")
            OrderType.UNKNOWN -> Log.d(TAG, "다시 말씀해주세요.")
        }
    }

    fun exampleYesOrNo() {
        val userInput = "어 그렇게 할게"
        val extractResult = detectSentiment(userInput)
        when (extractResult) {
            Sentiment.POSITIVE -> Log.d(TAG, "예 인식.")
            Sentiment.NEGATIVE -> Log.d(TAG, "아니오 인식.")
            Sentiment.UNKNOWN -> Log.d(TAG, "다시 말씀해주세요.")
        }
    }
}
