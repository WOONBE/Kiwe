package com.kiwe.payment_termianl

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.ToneGenerator
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var nfcAdapter: NfcAdapter
    private val apiService = RetrofitClient.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // NFC 어댑터 초기화
        initNfcAdapter()

    }

    // NFC 태그 감지 시 호출되는 메소드
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)


        lifecycleScope.launch {
            try {
                val response = apiService.sendPaymentRequest(BuildConfig.KIOSK_ID.toString())
                Log.d("응답", "${response}")
                if (response.code() == 200) {
                    playSuccessSound()
                } else {
//                    playFailureSound()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    "에러 발생: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // 성공 사운드 (짧고 높은 톤)
    private fun playSuccessSound() {
        val toneGenerator = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)
        toneGenerator.startTone(ToneGenerator.TONE_PROP_ACK, 300)  // 짧고 높은 톤
        toneGenerator.release()
    }

    // 실패 사운드 (길고 낮은 톤)
    private fun playFailureSound() {
        val toneGenerator = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)
        toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP2, 600)  // 길고 낮은 톤
        toneGenerator.release()
    }



    private fun initNfcAdapter() {
        // NFC 어댑터 초기화
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        if (nfcAdapter == null || !nfcAdapter.isEnabled) {
            Toast.makeText(this, "NFC를 지원하지 않거나 비활성화 되어 있습니다.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()

        // NFC 태그 인식을 위한 활성화
        val intent = Intent(this, javaClass).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP // 현재 액티비티에서 처리
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val filters = arrayOf(
            IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)  // 모든 NFC 태그를 처리
        )

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, null)
    }

    override fun onPause() {
        super.onPause()
        // NFC 기능 비활성화
        nfcAdapter.disableForegroundDispatch(this)
    }

}
