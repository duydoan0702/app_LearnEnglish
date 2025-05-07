package com.mobiai.app.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.mobiai.BuildConfig
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.min

fun Bitmap.toFile(
    path: String,
    compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG
) {
    ByteArrayOutputStream().use { baso ->
        compress(compressFormat, 100, baso)

        val file = File(path)
        if (!file.exists()) {
            file.createNewFile()
        }

        FileOutputStream(file).use {
            it.write(baso.toByteArray())
        }
    }
}



/**
 * this folder save image before call api enhance(paint, art, animation, ...)
 */
const val photoEnhance  = "Photo_Enhance"

/**
 * this folder save image after remove background và được save
 */
const val photoRemoveBackground  = "Photo_BG_remove"


const val  photoTemp = "Photo_Temp"
fun Bitmap.saveImageToFile(context: Context , folderName : String) : String?{

    val directory = File(context.filesDir, "/$folderName")
    Log.d("saveImageToFile", "saveImageToFile: $directory")
    if (!directory.exists()) {
        directory.mkdirs()
    }
    val fileName = "BackgroundEraser_" + generateFileName() + ".png"
    val file = File(directory, fileName)
    return try {
        FileOutputStream(file).use { out ->
            if (!this.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                throw IOException("Couldn't save this bitmap")
            }
        }
        file.absolutePath
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

const val FOLDER_EXTERNAL = "Background Eraser"
fun isApi29orHigher(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
}
fun Context.uriToFilePath(uri: Uri): String? {
    var filePath: String? = null
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = contentResolver.query(uri, projection, null, null, null)
    if (cursor != null && cursor.moveToFirst()) {
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        filePath = cursor.getString(columnIndex)
        cursor.close()
    }
    return filePath
}


//TODO check deprecated
fun Bitmap.saveImageExternal(context: Context) : String?{
    val directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "/$FOLDER_EXTERNAL"); //File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM), "/Background Eraser")
    Log.d("saveImageToFile", "saveImageToFile: $directory")
    if (!directory.exists()) {
        directory.mkdirs()
    }
    val fileName = "BackgroundEraser_" + generateFileName() + ".png"
    val file = File(directory, fileName)
    return try {
        FileOutputStream(file).use { out ->
            context.sendBroadcast(
                Intent(
                    "android.intent.action.MEDIA_SCANNER_SCAN_FILE",
                    Uri.fromFile(file)
                )
            )

            if (!this.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                throw IOException("Couldn't save this bitmap")
            }
        }
        file.absolutePath
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}
fun generateFileName(): String {
    val currentTimeMillis = System.currentTimeMillis()
    val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.getDefault())
    return dateFormat.format(currentTimeMillis)
}

fun Bitmap.resizeWithLimit(max: Int = 1024): Bitmap {
    val _width = min(width, max)
    val _height = min(height, max)

    if (width > height) {
        val newWidth = _width
        val newHeight = height * newWidth / width
        return Bitmap.createScaledBitmap(this, newWidth, newHeight, false)
    } else {
        val newHeight = _height
        val newWidth = width * newHeight / height
        return Bitmap.createScaledBitmap(this, newWidth, newHeight, false)
    }
}

fun Bitmap.resizeByArea(max: Int = 1024 * 1024) : Bitmap {
    try {
        val area = width * height;
        val a : Double = Math.sqrt(area.toDouble() / max)
        val newWidth = width / a
        val newHeight = height / a

        return Bitmap.createScaledBitmap(this, newWidth.toInt(), newHeight.toInt(), false)
//        return

    } catch (e: Exception) {
        return this
    }
}


fun Bitmap.resizeWithLimit2(max: Int = 1024): Bitmap {
    val _width = min(width, max)
    val _height = min(height, max)

    val number = 8

    if (width > height) {
        val newWidth = _width - (_width % number)
        var newHeight = height * newWidth / width
        newHeight -= (newHeight % number)
        return Bitmap.createScaledBitmap(this, newWidth, newHeight, false)
    } else {
        val newHeight = _height - (_width % number)
        var newWidth = width * newHeight / height
        newWidth -= (newWidth % number)
        return Bitmap.createScaledBitmap(this, newWidth, newHeight, false)
    }
}

fun Bitmap.release() {
    if (!isRecycled)  recycle()
}

fun Bitmap.trim(): Bitmap {
    var firstX = 0
    var firstY = 0
    var lastX = width
    var lastY = height
    if(width < 200 || height < 200){
        return this
    }
    val pixels = IntArray(width * height)
    getPixels(pixels, 0, width, 0, 0, width, height)
    loop@ for (x in 0 until width) {
        for (y in 0 until height) {
            if (pixels[x + y * width] != Color.TRANSPARENT) {
                firstX = x
                break@loop
            }
        }
    }
    loop@ for (y in 0 until height) {
        for (x in firstX until width) {
            if (pixels[x + y * width] != Color.TRANSPARENT) {
                firstY = y
                break@loop
            }
        }
    }
    loop@ for (x in width - 1 downTo firstX) {
        for (y in height - 1 downTo firstY) {
            if (pixels[x + y * width] != Color.TRANSPARENT) {
                lastX = x
                break@loop
            }
        }
    }
    loop@ for (y in height - 1 downTo firstY) {
        for (x in width - 1 downTo firstX) {
            if (pixels[x + y * width] != Color.TRANSPARENT) {
                lastY = y
                break@loop
            }
        }
    }
    return Bitmap.createBitmap(this, firstX, firstY, lastX - firstX, lastY - firstY)
}
