package com.example.textreader.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ImageUtils {
    fun saveImageToInternalStorage(context: Context, bitmap: Bitmap, noteId: Long): String? {
        return try {
            val directory = File(context.filesDir, "note_images")
            if (!directory.exists()) directory.mkdirs()

            val fileName = "image_${noteId}_${System.currentTimeMillis()}.jpg"
            val file = File(directory, fileName)

            FileOutputStream(file).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos)
            }

            file.absolutePath
        } catch (e: IOException) {
            Toast.makeText(context, "Ошибка сохранения изображения", Toast.LENGTH_SHORT).show()
            null
        }
    }

    fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
        return try {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        } catch (e: IOException) {
            Toast.makeText(context, "Ошибка загрузки изображения", Toast.LENGTH_SHORT).show()
            null
        }
    }

    fun loadImageFromStorage(context: Context, imagePath: String): Bitmap? {
        return try {
            val file = File(imagePath)
            if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null
        } catch (e: Exception) {
            null
        }
    }

    fun deleteImageFile(imagePath: String?) {
        if (imagePath.isNullOrEmpty()) return
        try {
            File(imagePath).takeIf { it.exists() }?.delete()
        } catch (e: Exception) {
            // Ignore
        }
    }
}