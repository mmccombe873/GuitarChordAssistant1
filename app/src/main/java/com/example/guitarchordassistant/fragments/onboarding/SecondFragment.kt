package com.example.guitarchordassistant.fragments.onboarding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.guitarchordassistant.R
import kotlinx.android.synthetic.main.fragment_first.view.*
import kotlinx.android.synthetic.main.fragment_first.view.next
import kotlinx.android.synthetic.main.fragment_second.view.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_second, container, false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        view.next.setOnClickListener {
            viewPager?.currentItem = 2
        }

        view.progressBar2.progress = 66

        view.progressText2.text = "2/3"

        return view
    }

}