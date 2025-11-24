package com.moodcam.frontend_android.helpers

import android.content.Context
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

/** Helper for memory-mapping TensorFlow Lite model files from assets. */

/** Maps a `.tflite` asset file into memory for TFLite interpreter use.
 * @param context App context.
 * @param modelName Asset file name.
 * @return Memory-mapped model buffer.
 */
fun loadModelFile(context: Context, modelName: String): MappedByteBuffer {
    val fileDescriptor = context.assets.openFd(modelName)
    val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
    val fileChannel = inputStream.channel
    val startOffset = fileDescriptor.startOffset
    val declaredLength = fileDescriptor.declaredLength
    return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
}
