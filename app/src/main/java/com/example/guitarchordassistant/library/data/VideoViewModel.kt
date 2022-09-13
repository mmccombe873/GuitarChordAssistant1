package com.example.guitarchordassistant.library.data

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideoViewModel(application: Application): AndroidViewModel(application) {

    fun deleteVideoFromInternalStorage(filename: String, application: Application): Boolean {
        return try {
            application.deleteFile(filename)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun loadVideosFromInternalStorage(application: Application): List<InternalStorageVideo> {
        return withContext(Dispatchers.IO) {
            val files = application.filesDir.listFiles()
            files?.filter { it.canRead() && it.isFile && it.name.endsWith(".mp4") }?.map {
                val uri = Uri.parse(it.name)
                InternalStorageVideo(it.name, uri)
            } ?: listOf()
        }
    }

}