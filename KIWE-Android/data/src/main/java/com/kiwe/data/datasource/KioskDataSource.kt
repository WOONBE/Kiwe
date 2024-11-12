package com.kiwe.data.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

private val Context.kioskDataStore: DataStore<Preferences> by preferencesDataStore(name = "kiosk_datastore")

class KioskDataSource
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) {
        /**
         * 소유자 ID 설정
         *
         * @param id 소유자의 ID
         */
        suspend fun setOwnerId(id: String) {
            context.kioskDataStore.edit { pref ->
                pref[OWNER_ID] = id
            }
        }

        /**
         * 소유자 ID 가져오기
         *
         * @return 소유자의 ID
         */
        suspend fun getOwnerId(): String =
            context.kioskDataStore.data
                .map { pref -> pref[OWNER_ID] ?: "" }
                .first()

        /**
         * 소유자 ID 삭제
         */
        suspend fun clearOwnerId() {
            context.kioskDataStore.edit { pref ->
                pref.remove(OWNER_ID)
            }
        }

        /**
         * Kiosk 이름을 설정하는 함수 (순번으로 받아옴)
         *
         * @param name Kiosk 이름 (순번으로 받아옴)
         */
        suspend fun setKioskName(name: String) {
            context.kioskDataStore.edit { pref ->
                pref[KIOSK_NAME] = name
            }
        }

        /**
         * Kiosk 이름을 가져오는 함수
         *
         * @return Kiosk 이름
         */
        suspend fun getKioskName(): String =
            context.kioskDataStore.data
                .map { pref -> pref[KIOSK_NAME] ?: "" }
                .first()

        /**
         * Kiosk 이름을 지우는 함수
         *
         */
        suspend fun clearKioskName() {
            context.kioskDataStore.edit { pref ->
                pref.remove(KIOSK_NAME)
            }
        }

        /**
         * 키오스크 ID 설정
         *
         * @param kioskId kioskId
         */
        suspend fun setKioskId(kioskId: String) {
            context.kioskDataStore.edit { pref ->
                pref[KIOSK_ID] = kioskId
            }
        }

        /**
         *  키오스크 ID 가져오기
         *
         * @return kioskId
         */
        suspend fun getKioskId(): String =
            context.kioskDataStore.data
                .map { pref ->
                    pref[KIOSK_ID] ?: ""
                }.first()

        /**
         * 키오스크 ID 지우기
         */
        suspend fun clearKioskId() {
            context.kioskDataStore.edit { pref ->
                pref.remove(KIOSK_ID)
            }
        }

        /**
         * Order Number 가져오기, 1씩 증가시키고 999 넘으면 1로 초기화
         *
         * @return 증가된 주문번호
         */
        suspend fun getOrderNumber(): Int {
            resetOrderNumberIfNeeded() // 필요한 경우 초기화

            val currentOrderNumber =
                context.kioskDataStore.data
                    .map { pref ->
                        pref[ORDER_NUMBER] ?: 1 // 기본값 1
                    }.first()

            val nextOrderNumber = if (currentOrderNumber >= 999) 1 else currentOrderNumber + 1

            context.kioskDataStore.edit { pref ->
                pref[ORDER_NUMBER] = nextOrderNumber
            }

            return nextOrderNumber
        }

        /**
         * Order Number 초기화
         */
        suspend fun clearOrderNumber() {
            context.kioskDataStore.edit { pref ->
                pref[ORDER_NUMBER] = 1
            }
        }

        /**
         * 필요한 경우 Order Number 초기화
         */
        private suspend fun resetOrderNumberIfNeeded() {
            val lastResetDate =
                context.kioskDataStore.data
                    .map { pref -> pref[LAST_RESET_DATE] ?: 0L }
                    .first()
            val currentDate = getCurrentDate()

            if (lastResetDate != currentDate) {
                clearOrderNumber() // 하루가 지났다면 Order Number를 1로 초기화
                context.kioskDataStore.edit { pref ->
                    pref[LAST_RESET_DATE] = currentDate // 초기화 후 날짜 갱신
                }
            }
        }

        /**
         * 현재 날짜를 가져오는 함수 (날짜 비교를 위해)
         *
         * @return 현재 날짜
         */
        private fun getCurrentDate(): Long {
            // LocalDate를 사용하여 현재 날짜를 UTC 기준 시간으로 가져옵니다.
            return LocalDate.now(ZoneId.systemDefault()).toEpochDay()
        }

        companion object {
            private val OWNER_ID = stringPreferencesKey("owner_id")
            private val KIOSK_NAME = stringPreferencesKey("kiosk_name")
            private val KIOSK_ID = stringPreferencesKey("kiosk_id")
            private val ORDER_NUMBER = intPreferencesKey("order_number")
            private val LAST_RESET_DATE = longPreferencesKey("last_reset_date")
        }
    }
