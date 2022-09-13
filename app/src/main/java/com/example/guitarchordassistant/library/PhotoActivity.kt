package com.example.guitarchordassistant.library

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.guitarchordassistant.R
import com.example.guitarchordassistant.databinding.ActivityPhotoBinding

class PhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Check if action bar is present
        assert(
            supportActionBar != null
        )
        // Show back button on action bar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = this.resources.getColor(R.color.colorPrimaryDark, theme)
        window.navigationBarColor = this.resources.getColor(R.color.colorPrimaryDark, theme)

        val extras: Bundle? = intent.extras
        var photoName = extras?.get("photo").toString()

        if (extras != null) {
            val files = filesDir.listFiles()
            files?.filter { it.canRead() && it.isFile && it.name.equals(photoName) }?.map {
                val bytes = it.readBytes()
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                binding.photoView.setImageBitmap(bmp)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}