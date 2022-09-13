package com.example.guitarchordassistant

import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_view_pager.view.*

class ViewPagerAdapter(val images: List<Int>)
    : RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {

    inner class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount() : Int {
        return images.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerAdapter.ViewPagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_pager, parent, false)
        return ViewPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val context = holder.itemView.context
        val curImage = images[position]
        holder.itemView.ivImage.setImageResource(curImage)

        holder.itemView.openCameraButton.setOnClickListener {
            val intent = Intent(context, CameraActivity::class.java)
            intent.putExtra("curImage", curImage)
            context.startActivity(intent)
        }
    }
}