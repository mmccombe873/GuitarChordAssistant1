package com.example.guitarchordassistant.library.data

import android.app.Application
import android.graphics.BitmapFactory
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotoViewModel(application: Application): AndroidViewModel(application) {

    fun deletePhotoFromInternalStorage(filename: String, application: Application): Boolean {
        return try {
            application.deleteFile(filename)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun loadPhotosFromInternalStorage(application: Application): List<InternalStoragePhoto> {
        return withContext(Dispatchers.IO) {
            val files = application.filesDir.listFiles()
            files?.filter { it.canRead() && it.isFile && it.name.endsWith(".jpg") }?.map {
                val bytes = it.readBytes()
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                InternalStoragePhoto(it.name, bmp)
            } ?: listOf()
        }
    }
}