package com.example.guitarchordassistant.library

import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.widget.MediaController
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.guitarchordassistant.R
import com.example.guitarchordassistant.databinding.ActivityVideoBinding
import java.io.File

class VideoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
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
        if(extras != null) {
            val filepath = extras.get("filepath").toString()
            val file = File(filepath)
            val videoView = binding.videoView
            val mc = MediaController(this)
            mc.setAnchorView(videoView)
            mc.setMediaPlayer(videoView)
            val uri = Uri.fromFile(file)
            videoView.setMediaController(mc)
            videoView.setVideoURI(uri)

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}