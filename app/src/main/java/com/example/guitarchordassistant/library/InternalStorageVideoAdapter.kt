package com.example.guitarchordassistant.library

import android.app.DownloadManager
import android.content.*
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.guitarchordassistant.databinding.ItemVideoBinding
import com.example.guitarchordassistant.library.data.InternalStorageVideo
import java.io.File


class InternalStorageVideoAdapter(
    private val onVideoClick: (InternalStorageVideo) -> Unit
) : ListAdapter<InternalStorageVideo, InternalStorageVideoAdapter.VideoViewHolder>(Companion) {

    inner class VideoViewHolder(val binding: ItemVideoBinding): RecyclerView.ViewHolder(binding.root) {
        var context: Context = binding.root.context
        var download: Long = 0
        var br = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                var id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == download) {
                    Toast.makeText(context, "Downloading complete", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    companion object : DiffUtil.ItemCallback<InternalStorageVideo>() {
        override fun areItemsTheSame(oldItem: InternalStorageVideo, newItem: InternalStorageVideo): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: InternalStorageVideo, newItem: InternalStorageVideo): Boolean {
            return oldItem.name == newItem.name && oldItem.uri == newItem.uri
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InternalStorageVideoAdapter.VideoViewHolder {
          return VideoViewHolder(
            ItemVideoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = currentList[position]
        val filepath = "/data/data/com.example.roomapp/files/" + video.name
        val context = holder.binding.ivVideo.context

        holder.binding.apply {
            Glide.with(context)
                .asBitmap()
                .load(Uri.fromFile(File(filepath)))
                .into(ivVideo)

            videoName.text = video.name

            playButton.setOnClickListener {
                val intent = Intent(context, VideoActivity::class.java)
                intent.putExtra("filepath", filepath)
                context.startActivity(intent)
            }

            menuButton.setOnClickListener {
                val popupMenu = PopupMenu(menuButton.context, menuButton)
                popupMenu.inflate(com.example.guitarchordassistant.R.menu.popup_menu)
                popupMenu.show()
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        com.example.guitarchordassistant.R.id.delete -> {
                            deleteVideoFromInternalStorage(context, video)
                        }
                    }
                    true
                }
            }
        }
    }

    private fun deleteVideoFromInternalStorage(context: Context, video: InternalStorageVideo) {
        val isDeletionSuccessful = context.deleteFile(video.name)
        if (isDeletionSuccessful) {
            Toast.makeText(context, "Video successfully deleted", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, VideoLibraryActivity::class.java)
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "Failed to delete video", Toast.LENGTH_SHORT).show()
        }
    }
}

