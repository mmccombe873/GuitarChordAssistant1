package com.example.guitarchordassistant.library

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.guitarchordassistant.R
import com.example.guitarchordassistant.databinding.ItemPhotoBinding
import com.example.guitarchordassistant.library.data.InternalStoragePhoto
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class InternalStoragePhotoAdapter(
    private val onPhotoClick: (InternalStoragePhoto) -> Unit
) : ListAdapter<InternalStoragePhoto, InternalStoragePhotoAdapter.PhotoViewHolder>(Companion) {

    inner class PhotoViewHolder(val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object : DiffUtil.ItemCallback<InternalStoragePhoto>() {
        override fun areItemsTheSame(
            oldItem: InternalStoragePhoto,
            newItem: InternalStoragePhoto
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: InternalStoragePhoto,
            newItem: InternalStoragePhoto
        ): Boolean {
            return oldItem.name == newItem.name && oldItem.bmp.sameAs(newItem.bmp)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            ItemPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = currentList[position]
        holder.binding.apply {
            val context = holder.binding.ivPhoto.context
            ivPhoto.setImageBitmap(photo.bmp)
            imageName.text = photo.name

            val aspectRatio = photo.bmp.width.toFloat() / photo.bmp.height.toFloat()
            ConstraintSet().apply {
                clone(root)
                setDimensionRatio(ivPhoto.id, aspectRatio.toString())
                applyTo(root)
            }

            ivPhoto.setOnClickListener {
                val intent = Intent(context, PhotoActivity::class.java)
                intent.putExtra("photo", photo.name)
                context.startActivity(intent)
            }

            menuButton.setOnClickListener {
                val popupMenu = PopupMenu(menuButton.context, menuButton)
                popupMenu.inflate(R.menu.popup_menu)
                popupMenu.show()
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.download -> {
                            downloadPhotoToExternalStorage(context, photo)
                        }
                        R.id.delete -> {
                            deletePhotoFromInternalStorage(context, photo)
                        }
                    }
                    true
                }
            }
        }
    }

    private fun deletePhotoFromInternalStorage(context: Context, photo: InternalStoragePhoto) {
        val isDeletionSuccessful = context.deleteFile(photo.name)
        if (isDeletionSuccessful) {
            Toast.makeText(context, "Photo successfully deleted", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, PhotoLibraryActivity::class.java)
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "Failed to delete photo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun downloadPhotoToExternalStorage(context: Context, photo: InternalStoragePhoto) {
        val filename = photo.name
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? = resolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }
        fos?.use {
            photo.bmp.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(context, "Saved to Gallery", Toast.LENGTH_SHORT).show()
        }
    }
}
