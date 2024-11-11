package com.kiwe.kiosk.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.ImageProcessingOptions
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarker
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import timber.log.Timber
import java.io.ByteArrayOutputStream

var initialTrackingId: Int? = null

@ExperimentalGetImage
fun processImageProxyFromCamera(
    context: Context,
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
                if (faces.isEmpty()) {
                    initialTrackingId = null
                    faceDetection(false)
                    Timber.tag("FaceTracking").d("얼굴 사라져서 initialTrackingId 초기화")
                    return@addOnSuccessListener
                }

                for (face in faces) {
                    // 첫 번째 얼굴의 trackingId만 설정하고 유지
                    val trackingId = face.trackingId
                    if (initialTrackingId == null && trackingId != null) {
                        initialTrackingId = trackingId
                        Timber.tag("Tracking ID").d("Initial Tracking ID set to: $trackingId")
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
                        Timber.tag("FC").d("Face width: $faceWidth, height: $faceHeight")

                        val bitmap = imageProxyToBitmap(imageProxy)
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

                        val faceLandmarker = FaceLandmarker.createFromOptions(context, landmarkerOptions)

                        val imageProcessingOptions =
                            ImageProcessingOptions
                                .builder()
                                .setRotationDegrees(imageProxy.imageInfo.rotationDegrees)
                                .build()

                        val result = faceLandmarker.detect(mpImage, imageProcessingOptions)
                        try {
                            val blendList = result.faceBlendshapes()
                            val shapeList = blendList.get().flatMap { innerList -> innerList.orEmpty() }

                            for (shape in shapeList) {
                                Timber.tag("BlendShape").d("Blend shape: ${shape.categoryName()} || ${shape.score()} || ${shape.index()}")
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        faceLandmarker.close()
                    } else {
                        Timber.tag("Tracking ID").d("Ignored face with ID: $trackingId")
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

fun imageProxyToBitmap(imageProxy: ImageProxy): Bitmap {
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
