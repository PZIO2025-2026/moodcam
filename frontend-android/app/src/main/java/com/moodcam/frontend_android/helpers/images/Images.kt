package com.moodcam.frontend_android.helpers.images

import android.graphics.*
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import androidx.core.graphics.scale
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder
import androidx.core.graphics.get

private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
    val yBuffer = image.planes[0].buffer
    val uBuffer = image.planes[1].buffer
    val vBuffer = image.planes[2].buffer

    val ySize = yBuffer.remaining()
    val uSize = uBuffer.remaining()
    val vSize = vBuffer.remaining()

    val nv21 = ByteArray(ySize + uSize + vSize)
    yBuffer.get(nv21, 0, ySize)
    vBuffer.get(nv21, ySize, vSize)
    uBuffer.get(nv21, ySize + vSize, uSize)

    val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
    val out = java.io.ByteArrayOutputStream()
    yuvImage.compressToJpeg(Rect(0, 0, image.width, image.height), 100, out)
    val imageBytes = out.toByteArray()
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}

// Optimized: Work directly with ImageProxy instead of converting to Bitmap first
@OptIn(ExperimentalGetImage::class)
fun detectLargestFace(
    imageProxy: ImageProxy,
    onFaceDetected: (Face?) -> Unit
) {
    val inputImage = InputImage.fromMediaImage(
        imageProxy.image!!,
        imageProxy.imageInfo.rotationDegrees
    )

    val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .build()
    val detector = FaceDetection.getClient(options)

    detector.process(inputImage)
        .addOnSuccessListener { faces ->
            val largestFace = faces.maxByOrNull { it.boundingBox.width() * it.boundingBox.height() }
            onFaceDetected(largestFace)
        }
        .addOnFailureListener {
            it.printStackTrace()
            onFaceDetected(null)
        }
}
fun cropAndResizeFace(bitmap: Bitmap, face: Face, size: Int = 48): Bitmap {
    val box = face.boundingBox
    val x = box.left.coerceAtLeast(0)
    val y = box.top.coerceAtLeast(0)
    val width = box.width().coerceAtMost(bitmap.width - x)
    val height = box.height().coerceAtMost(bitmap.height - y)
    val cropped = Bitmap.createBitmap(bitmap, x, y, width, height)
    return cropped.scale(size, size)
}
fun bitmapToGrayByteBuffer(bitmap: Bitmap): ByteBuffer {
    val inputBuffer = ByteBuffer.allocateDirect(1 * bitmap.width * bitmap.height * 1 * 4)
    inputBuffer.order(ByteOrder.nativeOrder())

    for (y in 0 until bitmap.height) {
        for (x in 0 until bitmap.width) {
            val pixel = bitmap[x, y]
            val r = (pixel shr 16 and 0xFF).toFloat()
            val g = (pixel shr 8 and 0xFF).toFloat()
            val b = (pixel and 0xFF).toFloat()
            val gray = (0.299f * r + 0.587f * g + 0.114f * b) / 255f
            inputBuffer.putFloat(gray)
        }
    }
    inputBuffer.rewind()
    return inputBuffer
}

fun processImageProxy(
    image: ImageProxy,
    tflite: Interpreter,
    onEmotionDetected: (String) -> Unit
) {
    // Detect face directly from ImageProxy (optimized)
    detectLargestFace(image) { face ->
        if (face != null) {
            // Only convert to Bitmap when we have a face to crop
            val bitmap = imageProxyToBitmap(image)
            val faceBitmap = cropAndResizeFace(bitmap, face)
            val inputBuffer = bitmapToGrayByteBuffer(faceBitmap)

            val output = Array(1) { FloatArray(7) }
            tflite.run(inputBuffer, output)

            val labels = listOf("Angry", "Disgust", "Fear", "Happy", "Neutral", "Sad", "Surprise")
            val maxIdx = output[0].indices.maxByOrNull { output[0][it] } ?: 0
            onEmotionDetected(labels[maxIdx])
        } else {
            onEmotionDetected("NoFace")
        }
        image.close()
    }
}