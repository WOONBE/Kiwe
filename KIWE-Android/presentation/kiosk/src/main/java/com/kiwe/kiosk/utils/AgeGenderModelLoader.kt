package com.kiwe.kiosk.utils

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer

class AgeGenderModelLoader(
    context: Context,
) {
    val ageModelInterpreter: Interpreter
    val genderModelInterpreter: Interpreter

    init {
        ageModelInterpreter = loadModel(context, "model_lite_age_q.tflite")
        genderModelInterpreter = loadModel(context, "model_lite_gender_q.tflite")
    }

    private fun loadModel(
        context: Context,
        modelName: String,
    ): Interpreter {
        val assetFileDescriptor = context.assets.openFd(modelName)
        val inputStream = assetFileDescriptor.createInputStream()
        val byteBuffer = ByteBuffer.allocateDirect(assetFileDescriptor.declaredLength.toInt())
        inputStream.channel.read(byteBuffer)
        byteBuffer.flip()
        return Interpreter(byteBuffer)
    }

    fun close() {
        ageModelInterpreter.close()
        genderModelInterpreter.close()
    }
}
