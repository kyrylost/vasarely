package com.example.vasarely.viewmodel.secondary

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Looper
import android.util.Log
import java.io.ByteArrayOutputStream
import kotlin.concurrent.thread

interface ImageInterface {

    fun rotateImage(source: Bitmap, angle: Float) : Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    fun compressBitmap(bitmap: Bitmap, quality: Int) : Bitmap {
        Log.d("compressBitmap",  (Looper.myLooper() == Looper.getMainLooper()).toString())

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        val byteArray = stream.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

}