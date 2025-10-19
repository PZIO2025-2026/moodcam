package com.moodcam.frontend_android.helpers.images

import android.graphics.*
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import androidx.core.graphics.scale
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder
import androidx.core.graphics.get
import androidx.core.graphics.createBitmap

private const val TAG = "EmotionPreprocessing"

private fun imageProxyToGrayBitmap(image: ImageProxy): Bitmap {
    val yPlane = image.planes[0]
    val yBuffer = yPlane.buffer.duplicate()
    
    val width = image.width
    val height = image.height
    val rowStride = yPlane.rowStride
    val pixelStride = yPlane.pixelStride
    
    // Create grayscale bitmap
    val bitmap = createBitmap(width, height)
    val pixels = IntArray(width * height)
    
    var offset = 0
    for (row in 0 until height) {
        for (col in 0 until width) {
            val yValue = yBuffer.get(row * rowStride + col * pixelStride).toInt() and 0xFF
            // Create grayscale ARGB pixel (R=G=B for true grayscale)
            pixels[offset++] = (0xFF shl 24) or (yValue shl 16) or (yValue shl 8) or yValue
        }
    }
    
    bitmap.setPixels(pixels, 0, width, 0, 0, width, height)

    // Apply rotation to match face detection
    return rotateBitmap(bitmap, image.imageInfo.rotationDegrees)
}

// Rotate bitmap to match the rotation applied during face detection
private fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
    if (degrees == 0) return bitmap
    
    val matrix = Matrix()
    matrix.postRotate(degrees.toFloat())
    val rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    bitmap.recycle() // Free original bitmap
    return rotated
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
    val detector = FaceDetectorProvider.detector

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
/** Crop face by bounding box and resize to a square target size. */
fun cropAndResizeFace(bitmap: Bitmap, face: Face, size: Int = ImageDefaults.FACE_CROP_SIZE): Bitmap {
    val box = face.boundingBox
    val x = box.left.coerceAtLeast(0)
    val y = box.top.coerceAtLeast(0)
    val width = box.width().coerceAtMost(bitmap.width - x)
    val height = box.height().coerceAtMost(bitmap.height - y)
    val cropped = Bitmap.createBitmap(bitmap, x, y, width, height)
    return cropped.scale(size, size)
}


// Simplified: Convert grayscale bitmap to normalized float buffer
// Matches Python: face_norm = face_resized / 255.0
// Convert grayscale Bitmap to normalized float buffer using configured divisor. 
fun bitmapToGrayByteBuffer(bitmap: Bitmap): ByteBuffer {
    val inputBuffer = ByteBuffer.allocateDirect(bitmap.width * bitmap.height * Float.BYTES)
    inputBuffer.order(ByteOrder.nativeOrder())

    var minVal = 255f
    var maxVal = 0f
    var sumVal = 0f
    var count = 0

    for (y in 0 until bitmap.height) {
        for (x in 0 until bitmap.width) {
            val pixel = bitmap[x, y]
            // For grayscale bitmap (R=G=B), extract any channel
            // Using red channel for consistency
            val gray = ((pixel shr 16) and 0xFF).toFloat()
            
            // Normalize to [0, 1] range - MUST match Python preprocessing
            val normalized = gray / ImageDefaults.NORMALIZE_DIVISOR
            inputBuffer.putFloat(normalized)
            
            // Track stats for debugging
            if (normalized < minVal) minVal = normalized
            if (normalized > maxVal) maxVal = normalized
            sumVal += normalized
            count++
        }
    }
    
    inputBuffer.rewind()
    
    // Log preprocessing stats (only occasionally to avoid spam)
    if (count > 0 && Math.random() < ImageDefaults.LOG_PREPROCESS_SAMPLE_RATE) { // Log 1% of preprocessing
        val avgVal = sumVal / count
        Log.d(TAG, "Preprocessing stats - Min: $minVal, Max: $maxVal, Avg: $avgVal")
        Log.d(TAG, "First 5 values: ${(0 until 5).map { inputBuffer.getFloat(it * Float.BYTES) }}")
        inputBuffer.rewind()
    }
    
    return inputBuffer
}

fun processImageProxy(
    image: ImageProxy,
    tflite: Interpreter,
    onEmotionDetected: (String) -> Unit
) {
    // Detect face directly from ImageProxy (optimized)
    detectLargestFace(image) { face ->
        try {
            if (face != null) {
                // IMPROVED: Use Y plane directly for grayscale (no JPEG compression)
                val grayBitmap = imageProxyToGrayBitmap(image)
                val faceBitmap = cropAndResizeFace(grayBitmap, face)
                val inputBuffer = bitmapToGrayByteBuffer(faceBitmap)

                val labels = EmotionLabels.LABELS
                val output = Array(1) { FloatArray(labels.size) }
                tflite.run(inputBuffer, output)

                val maxIdx = output[0].indices.maxByOrNull { output[0][it] } ?: 0

                // Debug logging (occasional)
                if (Math.random() < ImageDefaults.LOG_PREDICTION_SAMPLE_RATE) { // Log 5% of predictions
                    Log.d(TAG, "Model output: ${output[0].joinToString { "%.3f".format(it) }}")
                    Log.d(TAG, "Predicted: ${labels[maxIdx]} (confidence: ${"%.3f".format(output[0][maxIdx])})")
                }

                onEmotionDetected(labels[maxIdx])
            } else {
                onEmotionDetected(EmotionLabels.NO_FACE)
            }
        } finally {
            image.close()
        }
    }
}