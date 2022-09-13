package com.example.guitarchordassistant

import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.guitarchordassistant.databinding.ActivityDetailBinding
import com.example.guitarchordassistant.databinding.ActivityDiagramBinding

class DiagramActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiagramBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiagramBinding.inflate(layoutInflater)
        setContentView(binding.root)
        assert(
            supportActionBar != null //null check
        )
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) //show back button

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = this.resources.getColor(R.color.colorPrimaryDark, theme)
        window.navigationBarColor = this.resources.getColor(R.color.colorPrimaryDark, theme)

        val diagram = BitmapFactory.decodeResource(this.resources, R.drawable.explanation2)
        binding.photoView.setImageBitmap(diagram)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}