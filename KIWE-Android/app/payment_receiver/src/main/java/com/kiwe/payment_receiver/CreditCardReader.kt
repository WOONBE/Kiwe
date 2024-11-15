package com.kiwe.payment_receiver

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.util.Log.e
import android.util.Log.i
import java.io.IOException
import java.lang.ref.WeakReference
import java.nio.charset.Charset
import java.util.Arrays

private const val TAG = "LoyaltyCardReader 싸피"

class CreditCardReader(
    terminalCallback: TerminalCallback,
) : NfcAdapter.ReaderCallback {
    // Weak reference to prevent retain loop. mAccountCallback is responsible for exiting
    // foreground mode before it becomes invalid (e.g. during onPause() or onStop()).
    private val mAccountCallback: WeakReference<TerminalCallback>

    interface TerminalCallback {
        fun onAccountReceived(account: String?)
    }

    /**
     * Callback when a new tag is discovered by the system.
     *
     *
     * Communication with the card should take place here.
     *
     * @param tag Discovered tag
     */
    override fun onTagDiscovered(tag: Tag) {
        i(TAG, "New tag discovered")
        // Android's Host-based Card Emulation (HCE) feature implements the ISO-DEP (ISO 14443-4)
        // protocol.
        //
        // In order to communicate with a device using HCE, the discovered tag should be processed
        // using the IsoDep class.
        val isoDep = IsoDep.get(tag)
        if (isoDep != null) {
            try {
                // Connect to the remote NFC device
                isoDep.connect()
                // Build SELECT AID command for our loyalty card service.
                // This command tells the remote device which service we wish to communicate with.
                i(TAG, "Requesting remote AID: " + TERMINAL_AID)
                val command = buildSelectApdu(TERMINAL_AID)
                // Send command to remote device
                val result = isoDep.transceive(command)
                i(TAG, "Sending: " + byteArrayToHexString(command))
                // If AID is successfully selected, 0x9000 is returned as the status word (last 2
                // bytes of the result) by convention. Everything before the status word is
                // optional payload, which is used here to hold the account number.
                val resultLength = result.size
                val statusWord = byteArrayOf(result[resultLength - 2], result[resultLength - 1])
                val payload = Arrays.copyOf(result, resultLength - 2)

                // The remote NFC device will immediately respond with its stored account number
                val apiUrl = String(payload, Charset.forName("UTF-8"))
                i(TAG, "Received: $apiUrl")
                // Inform CardReaderFragment of received account number
                mAccountCallback.get()!!.onAccountReceived(apiUrl)
            } catch (e: IOException) {
                e(TAG, "Error communicating with card: $e")
            }
        }
    }

    companion object {
        // AID for our loyalty card service.
        private const val TERMINAL_AID = "F222222222"

        // ISO-DEP command HEADER for selecting an AID.
        // Format: [Class | Instruction | Parameter 1 | Parameter 2]
        private const val SELECT_APDU_HEADER = "00A40400"

        // "OK" status word sent in response to SELECT AID command (0x9000)
        private val SELECT_OK_SW = byteArrayOf(0x90.toByte(), 0x00.toByte())

        /**
         * Build APDU for SELECT AID command. This command indicates which service a reader is
         * interested in communicating with. See ISO 7816-4.
         *
         * @param aid Application ID (AID) to select
         * @return APDU for SELECT AID command
         */
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
         * Utility class to convert a byte array to a hexadecimal string.
         *
         * @param bytes Bytes to convert
         * @return String, containing hexadecimal representation.
         */
        fun byteArrayToHexString(bytes: ByteArray): String {
            val hexArray = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
            val hexChars = CharArray(bytes.size * 2)
            var v: Int
            for (j in bytes.indices) {
                v = bytes[j].toInt() and 0xFF
                hexChars[j * 2] = hexArray[v ushr 4]
                hexChars[j * 2 + 1] = hexArray[v and 0x0F]
            }
            return String(hexChars)
        }

        /**
         * Utility class to convert a hexadecimal string to a byte string.
         *
         *
         * Behavior with input strings containing non-hexadecimal characters is undefined.
         *
         * @param s String containing hexadecimal characters to convert
         * @return Byte array generated from input
         */
        fun hexStringToByteArray(s: String): ByteArray {
            val len = s.length
            val data = ByteArray(len / 2)
            var i = 0
            while (i < len) {
                data[i / 2] =
                    (
                        (s[i].digitToIntOrNull(16)!!.shl(4)) +
                            s[i + 1].digitToIntOrNull(16)!!
                    ).toByte()
                i += 2
            }
            return data
        }
    }

    init {
        mAccountCallback = WeakReference(terminalCallback)
    }
}
