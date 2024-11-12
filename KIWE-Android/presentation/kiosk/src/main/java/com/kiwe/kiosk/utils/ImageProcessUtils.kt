package com.kiwe.kiosk.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import androidx.compose.ui.geometry.Offset
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.ImageProcessingOptions
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarker
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.math.tan

class ImageProcessUtils
    @Inject
    constructor(
        context: Context,
        private val onAgeGenderDetected: (age: Int, genderText: String) -> Unit,
    ) : AgeGenderCallback {
        private var initialTrackingId: Int? = null

        @ExperimentalGetImage
        fun processImageProxyFromCamera(
            context: Context,
            imageProxy: ImageProxy,
            faceDetection: (Boolean) -> Unit,
            gazeDetection: (Offset?) -> Unit,
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
                        if (faces.isEmpty()) {
                            initialTrackingId = null
                            faceDetection(false)
//                            Timber.tag("FaceTracking").d("얼굴 사라져서 initialTrackingId 초기화")
                            return@addOnSuccessListener
                        }

                        for (face in faces) {
                            // 첫 번째 얼굴의 trackingId만 설정하고 유지
                            val trackingId = face.trackingId
                            if (initialTrackingId == null && trackingId != null) {
                                initialTrackingId = trackingId
//                                Timber.tag("Tracking ID").d("Initial Tracking ID set to: $trackingId")
                            }

                            // initialTrackingId와 일치하는 얼굴만 처리
                            if (trackingId == initialTrackingId) {
                                val bounds = face.boundingBox
                                val faceWidth = bounds.width()
                                val faceHeight = bounds.height()

                                // 얼굴의 크기에 따른 콜백 호출
                                if (faceWidth >= 200 || faceHeight >= 200) {
                                    faceDetection(true)
                                } else {
                                    faceDetection(false)
                                }
//                                Timber.tag("FC").d("Face width: $faceWidth, height: $faceHeight")

                                val bitmap = imageProxyToBitmap(imageProxy)
                                detectAgeAndGender(bitmap, face)
                                val mpImage = BitmapImageBuilder(bitmap).build()
                                val baseOptions =
                                    BaseOptions
                                        .builder()
                                        .setModelAssetPath("face_landmarker.task")
                                        .build()

                                val landmarkerOptions =
                                    FaceLandmarker.FaceLandmarkerOptions
                                        .builder()
                                        .setBaseOptions(baseOptions)
                                        .setOutputFaceBlendshapes(true)
                                        .build()

                                val faceLandmarker =
                                    FaceLandmarker.createFromOptions(context, landmarkerOptions)

                                val imageProcessingOptions =
                                    ImageProcessingOptions
                                        .builder()
                                        .setRotationDegrees(imageProxy.imageInfo.rotationDegrees)
                                        .build()

                                val result = faceLandmarker.detect(mpImage, imageProcessingOptions)

                                val gazePoint = estimateGaze(face, image.width, image.height)
                                gazeDetection(gazePoint)
                                try {
                                    val blendList = result.faceBlendshapes()
                                    val shapeList =
                                        blendList.get().flatMap { innerList -> innerList.orEmpty() }

                                    for (shape in shapeList) {
//                                        Timber
//                                            .tag("BlendShape")
//                                            .d("Blend shape: ${shape.categoryName()} || ${shape.score()} || ${shape.index()}")
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                                faceLandmarker.close()
                            } else {
//                                Timber.tag("Tracking ID").d("Ignored face with ID: $trackingId")
                            }
                        }
                    }.addOnFailureListener { e ->
                        e.printStackTrace()
                    }.addOnCompleteListener {
                        imageProxy.close()
                    }
            } else {
                imageProxy.close()
            }
        }

        private fun imageProxyToBitmap(imageProxy: ImageProxy): Bitmap {
            val yBuffer = imageProxy.planes[0].buffer // Y
            val uBuffer = imageProxy.planes[1].buffer // U
            val vBuffer = imageProxy.planes[2].buffer // V

            val ySize = yBuffer.remaining()
            val uSize = uBuffer.remaining()
            val vSize = vBuffer.remaining()

            val nv21 = ByteArray(ySize + uSize + vSize)

            yBuffer.get(nv21, 0, ySize)
            vBuffer.get(nv21, ySize, vSize)
            uBuffer.get(nv21, ySize + vSize, uSize)

            val yuvImage = YuvImage(nv21, ImageFormat.NV21, imageProxy.width, imageProxy.height, null)
            val out = ByteArrayOutputStream()
            yuvImage.compressToJpeg(Rect(0, 0, imageProxy.width, imageProxy.height), 100, out)
            val imageBytes = out.toByteArray()
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        }

        fun estimateGaze(
            face: Face,
            imageWidth: Int,
            imageHeight: Int,
        ): Offset? {
            val leftEye = face.getLandmark(FaceLandmark.LEFT_EYE)?.position
            val rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE)?.position

            if (leftEye != null && rightEye != null) {
                // 두 눈의 중심 계산
                val eyeCenterX = (leftEye.x + rightEye.x) / 2f
                val eyeCenterY = (leftEye.y + rightEye.y) / 2f

                // 이미지 크기에 따른 정규화
                val normalizedX = eyeCenterX / imageWidth
                val normalizedY = eyeCenterY / imageHeight

                val yaw = face.headEulerAngleY // 좌우 회전 각도
                val pitch = face.headEulerAngleX // 상하 회전 각도

                // 각도를 라디안으로 변환
                val yawRadians = Math.toRadians(yaw.toDouble())
                val pitchRadians = Math.toRadians(pitch.toDouble())

                // 시선 벡터 계산
//        val gazeVectorX = sin(yawRadians).toFloat()
//        val gazeVectorY = -sin(pitchRadians).toFloat()
                val yawWeight = 1.1f // 좌우 민감도 조정
                val pitchWeight = 1.3f
                val gazeVectorX = tan(yawRadians).toFloat() * yawWeight
                val gazeVectorY = -tan(pitchRadians).toFloat() * pitchWeight
                // 스케일링 팩터 적용
                val scalingFactor = 1.2f
                val adjustedGazeX = normalizedX + gazeVectorX * scalingFactor
                val adjustedGazeY = normalizedY + gazeVectorY * scalingFactor

                // 좌표 반전 적용
                val invertedX = 1f - adjustedGazeX // 좌우 반전
//        val invertedY = 1f - adjustedGazeY // 상하 반전

                // 값 범위를 0과 1 사이로 제한
                val clampedX = invertedX.coerceIn(0f, 1f)
                val clampedY = adjustedGazeY.coerceIn(0f, 1f)

                return Offset(clampedX, clampedY)
            }

            return null
        }

        private val modelLoader = AgeGenderModelLoader(context = context)
        private val ageGenderProcessor =
            AgeGenderEstimationProcessor(
                modelLoader = modelLoader,
                callback = this,
            )

        private fun detectAgeAndGender(
            faceBitmap: Bitmap,
            face: Face,
        ) {
            ageGenderProcessor.detectAttributes(faceBitmap, face)
        }

        fun close() {
            modelLoader.close()
        }

        override fun onFaceDetected(
            face: Face,
            age: Int,
            gender: Int,
        ) {
            val genderText = if (gender == 0) "Male" else "Female"
            onAgeGenderDetected(age, genderText)
        }
    }
