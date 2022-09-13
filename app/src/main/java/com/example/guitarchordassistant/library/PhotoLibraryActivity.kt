package com.example.guitarchordassistant.library

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guitarchordassistant.R
import com.example.guitarchordassistant.databinding.ActivityPhotoLibraryBinding
import com.example.guitarchordassistant.library.data.PhotoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope


class PhotoLibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhotoLibraryBinding
    private lateinit var internalStoragePhotoAdapter: InternalStoragePhotoAdapter
    private lateinit var photoViewModel: PhotoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        assert(
            supportActionBar != null //null check
        )
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) //show back button

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = this.resources.getColor(R.color.colorPrimaryDark, theme)
        window.navigationBarColor = this.resources.getColor(R.color.colorPrimaryDark, theme)

        photoViewModel = ViewModelProvider(this).get(PhotoViewModel::class.java)

        internalStoragePhotoAdapter = InternalStoragePhotoAdapter {
            val isDeletionSuccessful = photoViewModel.deletePhotoFromInternalStorage(it.name, application)
            if(isDeletionSuccessful) {
                loadPhotosFromInternalStorageIntoRecyclerView()
                Toast.makeText(this, "Photo successfully deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to delete photo", Toast.LENGTH_SHORT).show()
            }
        }

        setupInternalStorageRecyclerView()
        loadPhotosFromInternalStorageIntoRecyclerView()

    }

    private fun setupInternalStorageRecyclerView() = binding.rvPrivatePhotos.apply {
        adapter = internalStoragePhotoAdapter
        layoutManager = LinearLayoutManager(this.context)
    }

    private fun loadPhotosFromInternalStorageIntoRecyclerView() {
        lifecycleScope.launch {
            val photos = photoViewModel.loadPhotosFromInternalStorage(application)
            internalStoragePhotoAdapter.submitList(photos)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}