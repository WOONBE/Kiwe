package com.kiwe.payment_termianl

import android.nfc.NfcAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var nfcAdapter: NfcAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // NFC Adapter 초기화
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC를 지원하지 않는 기기입니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        // NFC 기능이 활성화 되어 있는지 확인
        if (!nfcAdapter.isEnabled) {
            Toast.makeText(this, "NFC가 비활성화 되어 있습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
    }
}
