package com.kiwe.payment_termianl

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.runBlocking
import java.util.Arrays

private const val TAG = "NfcHceService 싸피"

class NfcHceService : HostApduService() {
    override fun processCommandApdu(
        apdu: ByteArray,
        bundle: Bundle?,
    ): ByteArray {
        // NFC 태그가 읽혔을 때 보내는 데이터를 정의합니다.
        Log.d("NFC", "APDU Command: ${apdu.joinToString()}")
        val kioskId = BuildConfig.KIOSK_ID

        val response = "https://k11d205.p.ssafy.io/api/view/orders/latest/$kioskId".toByteArray() // B폰에 전달할 URL

        return if (Arrays.equals(SELECT_APDU, apdu)) {
            val retrofit = RetrofitClient.create()
            runBlocking {
                retrofit.sendPaymentRequest(kioskId.toString())
            }
            Log.d(TAG, "processCommandApdu: 정상")
            concatArrays(response, SELECT_OK_SW)
        } else {
            Log.d(TAG, "processCommandApdu: 비정상")
            UNKNOWN_CMD_SW
        }
    }

    override fun onDeactivated(reason: Int) {
        Log.d("NFC", "NFC connection deactivated, reason: $reason")
    }

    companion object {
        // AID for our loyalty card service.
        private const val TERMINAL_AID = "F222222222"

        // ISO-DEP command HEADER for selecting an AID.
        // Format: [Class | Instruction | Parameter 1 | Parameter 2]
        private const val SELECT_APDU_HEADER = "00A40400"

        // "OK" status word sent in response to SELECT AID command (0x9000)
        private val SELECT_OK_SW = hexStringToByteArray("9000")

        // "UNKNOWN" status word sent in response to invalid APDU command (0x0000)
        private val UNKNOWN_CMD_SW = hexStringToByteArray("0000")
        private val SELECT_APDU = buildSelectApdu(TERMINAL_AID)
        // END_INCLUDE(processCommandApdu)

        fun buildSelectApdu(aid: String): ByteArray {
            // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]
            return hexStringToByteArray(
                SELECT_APDU_HEADER +
                    String.format(
                        "%02X",
                        aid.length / 2,
                    ) + aid,
            )
        }

        /**
         * Utility method to convert a byte array to a hexadecimal string.
         *
         * @param bytes Bytes to convert
         * @return String, containing hexadecimal representation.
         */
        fun byteArrayToHexString(bytes: ByteArray): String {
            val hexArray = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
            val hexChars = CharArray(bytes.size * 2) // Each byte has two hex characters (nibbles)
            var v: Int
            for (j in bytes.indices) {
                v = bytes[j].toInt() and 0xFF // Cast bytes[j] to int, treating as unsigned value
                hexChars[j * 2] = hexArray[v ushr 4] // Select hex character from upper nibble
                hexChars[j * 2 + 1] = hexArray[v and 0x0F] // Select hex character from lower nibble
            }
            return String(hexChars)
        }

        /**
         * Utility method to convert a hexadecimal string to a byte string.
         *
         *
         *
         * Behavior with input strings containing non-hexadecimal characters is undefined.
         *
         * @param s String containing hexadecimal characters to convert
         * @return Byte array generated from input
         * @throws java.lang.IllegalArgumentException if input length is incorrect
         */
        @Throws(IllegalArgumentException::class)
        fun hexStringToByteArray(s: String): ByteArray {
            val len = s.length
            require(len % 2 != 1) { "Hex string must have even number of characters" }
            val data = ByteArray(len / 2) // Allocate 1 byte per 2 hex characters
            var i = 0
            while (i < len) {
                // Convert each character into a integer (base-16), then bit-shift into place
                data[i / 2] =
                    (
                        (s[i].digitToIntOrNull(16)!!.shl(4)) +
                            s[i + 1].digitToIntOrNull(16)!!
                    ).toByte()
                i += 2
            }
            return data
        }

        /**
         * Utility method to concatenate two byte arrays.
         *
         * @param first First array
         * @param rest  Any remaining arrays
         * @return Concatenated copy of input arrays
         */
        fun concatArrays(
            first: ByteArray,
            vararg rest: ByteArray,
        ): ByteArray {
            var totalLength = first.size
            for (array in rest) {
                totalLength += array.size
            }
            val result = Arrays.copyOf(first, totalLength)
            var offset = first.size
            for (array in rest) {
                System.arraycopy(array, 0, result, offset, array.size)
                offset += array.size
            }
            return result
        }
    }
}
