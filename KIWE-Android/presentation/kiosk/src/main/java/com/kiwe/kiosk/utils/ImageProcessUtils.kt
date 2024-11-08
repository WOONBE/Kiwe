package com.kiwe.kiosk.utils

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import timber.log.Timber

@ExperimentalGetImage
fun processImageProxyFromCamera(
    imageProxy: ImageProxy,
    faceDetection: (Boolean) -> Unit,
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        val options =
            FaceDetectorOptions
                .Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .setMinFaceSize(1f)
                .enableTracking()
                .build()

        val detector = FaceDetection.getClient(options)

        detector
            .process(image)
            .addOnSuccessListener { faces: List<Face> ->
                for (face in faces) {
                    // 얼굴 감지 결과를 처리하는 코드
                    val bounds = face.boundingBox
                    val faceWidth = bounds.width()
                    val faceHeight = bounds.height()
                    // 200
                    if (faceWidth >= 200 || faceHeight >= 200) {
                        faceDetection(true)
                    } else {
                        faceDetection(false)
                    }
                    Timber.tag("FC").d("Face width: $faceWidth, height: $faceHeight")
                    val rotY = face.headEulerAngleY
                    val rotZ = face.headEulerAngleZ

                    // 여기서 추가 작업 가능
                    Timber.tag("FC").d("bounds: $bounds")
                    Timber.tag("FC").d("rotY: $rotY")
                    Timber.tag("FC").d("rotZ: $rotZ")
                }
            }.addOnFailureListener { e ->
                e.printStackTrace()
            }.addOnCompleteListener {
                imageProxy.close() // 반드시 이미지 프록시를 닫아야 메모리 누수가 없습니다.
            }
    } else {
        imageProxy.close() // 이미지가 null일 경우 닫기
    }
}
