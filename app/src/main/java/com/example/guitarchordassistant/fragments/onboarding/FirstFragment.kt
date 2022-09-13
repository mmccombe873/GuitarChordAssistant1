package com.example.guitarchordassistant.fragments.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.guitarchordassistant.R
import com.example.guitarchordassistant.databinding.FragmentFirstBinding
import com.example.guitarchordassistant.library.InternalStoragePhotoAdapter
import kotlinx.android.synthetic.main.fragment_first.view.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private lateinit var onboardingViewPagerAdapter: OnboardingViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_first, container, false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        view.next.setOnClickListener {
            viewPager?.currentItem = 1
        }

        view.progressBar.progress = 33

        view.progressText.text = "1/3"

        return view
    }
}