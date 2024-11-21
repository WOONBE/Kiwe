package com.kiwe.payment_receiver

import android.content.Intent
import android.net.Uri
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.kiwe.payment_receiver.ui.theme.KIWEAndroidTheme

private const val TAG = "MainActivity 싸피"

class MainActivity :
    ComponentActivity(),
    CreditCardReader.TerminalCallback {
    private lateinit var mLoyaltyCardReader: CreditCardReader
    private var text by mutableStateOf("기본")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            enableEdgeToEdge(
                navigationBarStyle = SystemBarStyle.dark(
                    R.color.black
                )
            )

            KIWEAndroidTheme {
                Surface {
                    Box(

                    ){
                        Image(
                            painterResource(R.drawable.card),
                            contentScale = ContentScale.FillHeight,
                            contentDescription = "",
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mLoyaltyCardReader = CreditCardReader(this)
        enableReaderMode()
    }

    override fun onPause() {
        super.onPause()
        disableReaderMode()
    }

    private fun enableReaderMode() {
        val nfc = NfcAdapter.getDefaultAdapter(this)
        nfc.enableReaderMode(this, mLoyaltyCardReader, READER_FLAGS, null)
    }

    private fun disableReaderMode() {
        val nfc = NfcAdapter.getDefaultAdapter(this)
        nfc.disableReaderMode(this)
    }

    // 웹 페이지 열기
    private fun openWebPage(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    companion object {
        // Recommend NfcAdapter flags for reading from other Android devices. Indicates that this
        // activity is interested in NFC-A devices (including other Android devices), and that the
        // system should not check for the presence of NDEF-formatted data (e.g. Android Beam).
        const val READER_FLAGS =
            NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK
    }

    override fun onAccountReceived(account: String?) {
        Log.d(TAG, "onAccountReceived: $account")
        text = account ?: ""
        openWebPage(text)
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KIWEAndroidTheme {
        Greeting("Android")
    }
}
