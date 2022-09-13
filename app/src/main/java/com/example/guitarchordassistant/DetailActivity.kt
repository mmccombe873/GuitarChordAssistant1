package com.example.guitarchordassistant

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager2.widget.ViewPager2
import com.example.guitarchordassistant.data.Image
import com.example.guitarchordassistant.databinding.ActivityDetailBinding
import kotlinx.android.synthetic.main.activity_detail.*
import java.nio.file.Files.size


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        assert(
            supportActionBar != null //null check
        )
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) //show back button

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = this.resources.getColor(R.color.colorPrimaryDark, theme)
            window.navigationBarColor = this.resources.getColor(R.color.colorPrimaryDark, theme)
        }

        val extras: Bundle? = intent.extras
        val chord_txt = binding.textView

        if (extras != null) {
            val chordName = extras.get("name")
            val sound = extras.get("sound").toString()
            val type = extras.get("type").toString()
            val images: MutableList<Image> = extras.getSerializable("images") as MutableList<Image>

            chord_txt.text = chordName.toString() + " " + type

            val resIdSound = resources.getIdentifier(sound, "raw", packageName)

            val adapter = ViewPagerAdapter(createImageIdList(images))
            viewPager.adapter = adapter
            viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    val currentItem = (viewPager.currentItem + 1).toString()
                    val totalItems = images.size.toString()
                    binding.currentItem.text = "$currentItem/$totalItems"
                }

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                }
            })


            val player : MediaPlayer = MediaPlayer.create(this, resIdSound)

            play_btn.setOnClickListener {
                if(!player.isPlaying) {
                    player.start()
                    play_btn.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
                } else {
                    player.pause()
                    play_btn.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
                }
            }

            player.setOnCompletionListener {
                play_btn.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
            }

        } else {
            chord_txt.text = "Error! Invalid chord selected"
        }

    }

    private fun createImageIdList(images: MutableList<Image>) : List<Int> {
        var imageIdList: MutableList<Int> = mutableListOf<Int>()
        for(image in images){
            val resId = resources.getIdentifier(image.imageName, "drawable", packageName)
            imageIdList.add(resId)
        }
        return imageIdList
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}