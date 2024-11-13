package com.kiwe.payment_termianl

import android.nfc.cardemulation.HostApduService
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log

class NfcHceService : HostApduService() {

    override fun processCommandApdu(apdu: ByteArray, bundle: Bundle?): ByteArray {
        // NFC 태그가 읽혔을 때 보내는 데이터를 정의합니다.
        Log.d("NFC", "APDU Command: ${apdu.joinToString()}")
        val response = "http://example.com".toByteArray()  // B폰에 전달할 URL
        return response  // B폰에 URL을 전달
    }

    override fun onDeactivated(reason: Int) {
        Log.d("NFC", "NFC connection deactivated, reason: $reason")
    }
}
