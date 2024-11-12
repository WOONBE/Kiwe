package com.kiwe.kiosk.utils

import android.graphics.Bitmap
import android.graphics.Rect
import com.google.mlkit.vision.face.Face
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import javax.inject.Singleton

interface AgeGenderCallback {
    fun onFaceDetected(
        face: Face,
        age: Int,
        gender: Int,
    )
}

@Singleton
class AgeGenderEstimationProcessor(
    private val modelLoader: AgeGenderModelLoader,
    private val callback: AgeGenderCallback,
) {
    private val ageImageProcessor =
        ImageProcessor
            .Builder()
            .add(ResizeOp(200, 200, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0f, 255f))
            .build()

    private val genderImageProcessor =
        ImageProcessor
            .Builder()
            .add(ResizeOp(128, 128, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0f, 255f))
            .build()

    fun detectAttributes(
        image: Bitmap,
        face: Face,
    ) {
        val faceBitmap = cropToBBox(image, face.boundingBox)
        if (faceBitmap != null) {
            val (age, gender) = analyzeAgeGender(faceBitmap)
            callback.onFaceDetected(face, age, gender)
        }
    }

    private fun analyzeAgeGender(faceBitmap: Bitmap): Pair<Int, Int> {
        val tensorImage = TensorImage.fromBitmap(faceBitmap)

        // Age estimation
        val ageBuffer = ageImageProcessor.process(tensorImage).buffer
        val ageOutput = Array(1) { FloatArray(1) }
        modelLoader.ageModelInterpreter.run(ageBuffer, ageOutput)
        val age = (ageOutput[0][0] * 116).toInt()

        // Gender estimation
        val genderBuffer = genderImageProcessor.process(tensorImage).buffer
        val genderOutput = Array(1) { FloatArray(2) }
        modelLoader.genderModelInterpreter.run(genderBuffer, genderOutput)
        val gender = if (genderOutput[0][0] > genderOutput[0][1]) 0 else 1 // 0 for Male, 1 for Female

        return age to gender
    }

    private fun cropToBBox(
        image: Bitmap,
        boundingBox: Rect,
    ): Bitmap? =
        if (boundingBox.top >= 0 &&
            boundingBox.bottom <= image.height &&
            boundingBox.left >= 0 &&
            boundingBox.right <= image.width
        ) {
            Bitmap.createBitmap(image, boundingBox.left, boundingBox.top, boundingBox.width(), boundingBox.height())
        } else {
            null
        }
}
