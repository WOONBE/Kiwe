package com.kiwe.kiosk.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import com.kiwe.kiosk.navigation.MainNavHost
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import com.kiwe.kiosk.utils.ImageProcessUtils
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var imageProcessUtil: ImageProcessUtils
    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val cameraGranted = permissions[Manifest.permission.CAMERA] ?: false
            val audioGranted = permissions[Manifest.permission.RECORD_AUDIO] ?: false
            @ExperimentalGetImage
            if (cameraGranted && audioGranted) {
                startCamera()
            } else {
                if (!audioGranted) {
                    Toast.makeText(this, "음성 권한을 허용해주세요", Toast.LENGTH_SHORT).show()
                }
                if (!cameraGranted) {
                    Toast.makeText(this, "카메라 권한을 허용해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        // 터치 이벤트가 발생했을 때 실행할 로직
        if (ev != null) {
            // 터치 이벤트의 종류를 확인 (예: DOWN, UP 등)
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 터치 시작 시 필요한 처리
                    onUserInteractionDetected()
                    Timber.tag("ACTION_DOWN").d("ACTION_DOWN")
                }
                MotionEvent.ACTION_UP -> {
                    // 터치 종료 시 필요한 처리
                    Timber.tag("ACTION_UP").d("ACTION_UP")
                }
                MotionEvent.ACTION_MOVE -> {
                    // 터치 이동 시 필요한 처리
                    Timber.tag("ACTION_MOVE").d("ACTION_MOVE")
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun onUserInteractionDetected() {
        Timber.tag("MainActivity").d("사용자 상호작용 감지")
        // 사용자 상호작용이 감지되었을 때 실행할 로직
        // 예: ViewModel을 통해 타이머 초기화
        mainViewModel.onStartTouchScreen()
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA,
        ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO,
            ) == PackageManager.PERMISSION_GRANTED

    @OptIn(ExperimentalGetImage::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        imageProcessUtil = ImageProcessUtils(this, mainViewModel::onDetectAgeGender)
        enableEdgeToEdge()

        setContent {
            KIWEAndroidTheme {
                MainNavHost()
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.apply {
                hide(systemBars())
            }
        }
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissionsLauncher.launch(
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),
            )
        }
    }

    @ExperimentalGetImage
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
//            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA // 좌표는 현재 후면카메라에 맞춰져있음

            val imageAnalysis =
                ImageAnalysis
                    .Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

            var lastAnalyzedTime = 0L
            val frameIntervalMillis = 500L
            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), { imageProxy ->
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastAnalyzedTime >= frameIntervalMillis) {
                    lastAnalyzedTime =
                        currentTime // 마지막 처리 시간 업데이트
                    imageProcessUtil.processImageProxyFromCamera(
                        context = this,
                        imageProxy = imageProxy,
                        faceDetection = { detect -> mainViewModel.detectPerson(detect) },
                        gazeDetection = { gazePoint -> mainViewModel.updateGazePoint(gazePoint) },
                    )
                } else {
                    imageProxy.close() // 처리하지 않은 프레임은 닫아줌
                }
            })

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    imageAnalysis,
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }
}
