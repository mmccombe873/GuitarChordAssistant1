package com.example.guitarchordassistant.fragments.list

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.guitarchordassistant.DetailActivity
import com.example.guitarchordassistant.R
import com.example.guitarchordassistant.data.Chord
import com.example.guitarchordassistant.data.ChordWithImages
import com.example.guitarchordassistant.data.Image
import kotlinx.android.synthetic.main.custom_row.view.*
import java.io.Serializable

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var chordList = emptyList<ChordWithImages>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false))
    }

    override fun getItemCount(): Int {
       return chordList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = chordList[position]
        val context = holder.itemView.context

        // Set the text of the ChordViewHolder
        holder.itemView.button.text = currentItem.chord.name
        holder.itemView.button.setOnClickListener {
            val images = chordList[position].images
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("name", chordList[position].chord.name)
            intent.putExtra("sound", chordList[position].chord.sound)
            intent.putExtra("type", chordList[position].chord.type)
            intent.putExtra("images", images as Serializable)
            context.startActivity(intent)
        }

    }

    fun setData(chordWithImages: List<ChordWithImages>){
        this.chordList = chordWithImages
        notifyDataSetChanged()
    }
}