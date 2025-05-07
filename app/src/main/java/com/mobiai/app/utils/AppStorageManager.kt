package com.mobiai.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

object AppStorageManager {

    private val TAG: String = AppStorageManager::javaClass.name
    private const val PARENT_DIR_NAME = "/avatar_asset"


    fun saveImageToInternal(fileDir: File, bitmap: Bitmap, format: CompressFormat ): String {
        try {
            FileOutputStream(fileDir).use { out ->
                if (!bitmap.compress(format, 100, out)) {
                    return ""
                }
                return fileDir.absolutePath
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return ""
        }
    }


    fun copyFile(inputFile: File , outputFile: File ) : String?{
        try {
            val inputStream = FileInputStream(inputFile)

            val outputStream = FileOutputStream(outputFile)
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }
            inputStream.close()
            outputStream.close()
            return outputFile.absolutePath
        } catch (e: IOException) {
            Log.i(TAG, "copyFile: ${e.message}")
            return null
        }
    }

    fun createInternalFolder(context: Context, folderName: String): File? {
        val parentFileDir = File(context.filesDir, PARENT_DIR_NAME)

        if (parentFileDir.exists() || parentFileDir.mkdir()) {
            val childPackDir = File(context.filesDir, "$PARENT_DIR_NAME/$folderName")
            if (childPackDir.exists() || childPackDir.mkdir()) {
                return childPackDir
            }
            Log.e(TAG, "createInternalFolder: cannot create child dir: $childPackDir")
        } else {
            Log.e(TAG, "createInternalFolder: cannot create parent dir : $parentFileDir")
        }
        return null
    }

    fun convertUriToBitmap(context: Context, imageUri: Uri, isRotate: Boolean, rotation : Int ): Bitmap? {
        val startTime  = System.currentTimeMillis()
        var bitmap: Bitmap? = null
        val bmp = try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            Log.e(TAG, "convertUriToBitmap: cannot get bitmap from uri ", e)
            return null
        }

        if (bmp != null) {
            bitmap = if (isRotate && rotation != 0) {
                rotateBitmap(bmp, true, rotation)
            } else {
                bmp
            }
        }
        Log.d(TAG, "convertUriToBitmap: ${System.currentTimeMillis() - startTime}, Thread: ")
        return bitmap
    }

    private fun rotateBitmap(bitmap: Bitmap, isRecycle: Boolean, degrees : Int): Bitmap {

        val mt = Matrix()
        mt.postRotate(degrees.toFloat())

        val recycleBitmap = Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            mt,
            true
        )
        if (isRecycle) {
            bitmap.recycle()
        }
        return recycleBitmap
    }

}