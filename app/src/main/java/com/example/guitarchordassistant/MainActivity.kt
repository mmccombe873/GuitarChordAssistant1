package com.example.guitarchordassistant

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.guitarchordassistant.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.fragment_list.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = this.resources.getColor(R.color.colorPrimaryDark, theme)
        window.navigationBarColor = this.resources.getColor(R.color.colorPrimaryDark, theme)

    }
}