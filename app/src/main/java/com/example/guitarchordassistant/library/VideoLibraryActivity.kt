package com.example.guitarchordassistant.library

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guitarchordassistant.R
import com.example.guitarchordassistant.databinding.ActivityVideoLibraryBinding
import com.example.guitarchordassistant.library.data.VideoViewModel
import kotlinx.coroutines.launch

class VideoLibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoLibraryBinding
    private lateinit var internalStorageVideoAdapter: InternalStorageVideoAdapter
    private lateinit var videoViewModel: VideoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        assert(
            supportActionBar != null //null check
        )
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) //show back button

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = this.resources.getColor(R.color.colorPrimaryDark, theme)
        window.navigationBarColor = this.resources.getColor(R.color.colorPrimaryDark, theme)

        videoViewModel = ViewModelProvider(this).get(VideoViewModel::class.java)

        internalStorageVideoAdapter = InternalStorageVideoAdapter {
            val isDeletionSuccessful = videoViewModel.deleteVideoFromInternalStorage(it.name, application)
            if(isDeletionSuccessful) {
                loadVideosFromInternalStorageIntoRecyclerView()
                Toast.makeText(this, "Photo successfully deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to delete photo", Toast.LENGTH_SHORT).show()
            }
        }

        setupInternalStorageRecyclerViewForVideos()
        loadVideosFromInternalStorageIntoRecyclerView()
    }

    private fun setupInternalStorageRecyclerViewForVideos() = binding.rvPrivateVideos.apply {
        adapter = internalStorageVideoAdapter
        layoutManager = LinearLayoutManager(this.context)
    }

    private fun loadVideosFromInternalStorageIntoRecyclerView() {
        lifecycleScope.launch {
            val videos = videoViewModel.loadVideosFromInternalStorage(application)
            internalStorageVideoAdapter.submitList(videos)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}