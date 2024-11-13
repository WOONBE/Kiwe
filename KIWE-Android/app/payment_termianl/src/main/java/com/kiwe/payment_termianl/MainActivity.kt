package com.kiwe.payment_termianl

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
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

        // NFC 태그가 감지될 때
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action || NfcAdapter.ACTION_TAG_DISCOVERED == intent.action) {
            val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            tag?.let {
                // NFC 태그에서 URL을 추출하는 방법
                // 예시 URL을 추출 (HCE를 통해 태그가 보낸 URL을 사용)
                val tagId = it.id.joinToString(":") { byte -> "%02x".format(byte) }
                Log.d("NFC", "태그 ID: $tagId")

                // HCE 카드 서비스에서 URL을 읽었으면, 이를 웹 페이지로 열기
                val webPageUrl = "http://example.com" // 이 부분을 HCE 서비스에서 보낸 URL로 대체
                openWebPage(webPageUrl)
            }
        }
    }

    // 웹 페이지 열기
    private fun openWebPage(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    // NFC 관련 이벤트 처리
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action || NfcAdapter.ACTION_TAG_DISCOVERED == intent.action) {
            val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            tag?.let {
                // 태그 데이터를 로그로 출력
                val tagId = it.id.joinToString(":") { byte -> "%02x".format(byte) }
                Log.d("NFC", "새로운 태그 ID: $tagId")
            }
        }
    }
}
