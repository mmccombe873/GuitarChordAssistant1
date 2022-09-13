package com.example.guitarchordassistant.fragments.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.guitarchordassistant.R
import kotlinx.android.synthetic.main.activity_detail.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [ViewPagerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        val fragmentList = arrayListOf<Fragment>(
            FirstFragment(),
            SecondFragment(),
            ThirdFragment()
        )

        val adapter = OnboardingViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        view.viewPager.adapter = adapter

        return view
    }

}