package com.example.guitarchordassistant

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity


class CagedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caged)
        assert(
            supportActionBar != null //null check
        )
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) //show back button

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = this.resources.getColor(R.color.colorPrimaryDark, theme)
        window.navigationBarColor = this.resources.getColor(R.color.colorPrimaryDark, theme)

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}