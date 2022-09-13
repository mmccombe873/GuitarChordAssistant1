package com.example.guitarchordassistant

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.guitarchordassistant.databinding.ActivityChordBinding

class ChordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBarWithNavController(findNavController(R.id.fragment))

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = this.resources.getColor(R.color.colorPrimaryDark, theme)
            window.navigationBarColor = this.resources.getColor(R.color.colorPrimaryDark, theme)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}