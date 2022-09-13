package com.example.guitarchordassistant.fragments.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.guitarchordassistant.*
import com.example.guitarchordassistant.library.PhotoLibraryActivity
import com.example.guitarchordassistant.library.VideoLibraryActivity
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        view.chordSelection.setOnClickListener {
            val intent = Intent(context, ChordActivity::class.java)
            startActivity(intent)
        }

        view.camera.setOnClickListener {
            val intent = Intent(context, CameraActivity::class.java)
            startActivity(intent)
        }

        view.photoGallery.setOnClickListener {
            val intent = Intent(context, PhotoLibraryActivity::class.java)
            startActivity(intent)
        }

        view.videoGallery.setOnClickListener {
            val intent = Intent(context, VideoLibraryActivity::class.java)
            startActivity(intent)
        }

        view.caged.setOnClickListener {
            val intent = Intent(context, CagedActivity::class.java)
            startActivity(intent)
        }

        view.diagrams.setOnClickListener {
            val intent = Intent(context, DiagramActivity::class.java)
            startActivity(intent)
        }

        return view
    }

}