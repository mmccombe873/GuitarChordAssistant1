package com.example.guitarchordassistant.fragments.list

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guitarchordassistant.CameraActivity
import com.example.guitarchordassistant.R
import com.example.guitarchordassistant.data.Chord
import com.example.guitarchordassistant.data.ChordViewModel
import com.example.guitarchordassistant.data.Image
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.coroutines.launch

class ListFragment : Fragment() {

    private lateinit var chordViewModel: ChordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        // Recyclerview
        val adapter = ListAdapter()
        val recyclerView = view.recyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        populateDatabase(adapter)

        return view
    }

    private fun populateDatabase(adapter: ListAdapter) {

        // ChordViewModel
        chordViewModel = ViewModelProvider(this).get(ChordViewModel::class.java)

        val chords = listOf(
            Chord(1, "A", "asound", "Major"),
            Chord(2, "Bb", "bbsound", "Major"),
            Chord(3, "B", "bsound", "Major"),
            Chord(4, "C", "csound", "Major"),
            Chord(5, "C#", "c_sound", "Major"),
            Chord(6, "D", "dsound", "Major"),
            Chord(7, "Eb", "ebsound", "Major"),
            Chord(8, "E", "esound", "Major"),
            Chord(9, "F", "fsound", "Major"),
            Chord(10, "F#", "f_sound", "Major"),
            Chord(11, "G", "gsound", "Major"),
            Chord(12, "Ab", "absound", "Major")
        )

        lifecycleScope.launch {
            chords.forEach { chordViewModel.insertChord(it) }
        }

        val images = listOf(
            Image(1, "amaja", 1),
            Image(2, "amajg", 1),
            Image(3, "amaje", 1),
            Image(4, "amajd", 1),
            Image(5, "amajc", 1),
            Image(6, "a_maja", 2),
            Image(7, "a_majg", 2),
            Image(8, "a_maje", 2),
            Image(9, "a_majd", 2),
            Image(10, "a_majc", 2),
            Image(11, "bmaja", 3),
            Image(12, "bmajg", 3),
            Image(13, "bmaje", 3),
            Image(14, "bmajd", 3),
            Image(15, "bmajc", 3),
            Image(16, "cmajc", 4),
            Image(17, "cmaja", 4),
            Image(18, "cmajg", 4),
            Image(19, "cmaje", 4),
            Image(20, "cmajd", 4),
            Image(21, "c_majc", 5),
            Image(22, "c_maja", 5),
            Image(23, "c_majg", 5),
            Image(24, "c_maje", 5),
            Image(25, "c_majd", 5),
            Image(26, "dmajd", 6),
            Image(27, "dmajc", 6),
            Image(28, "dmaja", 6),
            Image(29, "dmajg", 6),
            Image(30, "dmaje", 6),
            Image(31, "ebmajd", 7),
            Image(32, "ebmajc", 7),
            Image(33, "ebmaja", 7),
            Image(34, "ebmajg", 7),
            Image(35, "ebmaje", 7),
            Image(36, "emaje", 8),
            Image(37, "emajd", 8),
            Image(38, "emajc", 8),
            Image(39, "emaja", 8),
            Image(40, "emajg", 8),
            Image(41, "fmaje", 9),
            Image(42, "fmajd", 9),
            Image(43, "fmajc", 9),
            Image(44, "fmaja", 9),
            Image(45, "fmajg", 9),
            Image(46, "f_maje", 10),
            Image(47, "f_majd", 10),
            Image(48, "f_majc", 10),
            Image(49, "f_maja", 10),
            Image(50, "f_majg", 10),
            Image(51, "gmajg", 11),
            Image(52, "gmaje", 11),
            Image(53, "gmajd", 11),
            Image(54, "gmajc", 11),
            Image(55, "gmaja", 11),
            Image(56, "abmajg", 12),
            Image(57, "abmaje", 12),
            Image(58, "abmajd", 12),
            Image(59, "abmajc", 12),
            Image(60, "abmaja", 12)
        )

        lifecycleScope.launch {
            images.forEach{ chordViewModel.insertImage(it) }
        }

        lifecycleScope.launch {
            chordViewModel.readAllData.observe(viewLifecycleOwner, Observer { chordWithImages ->
                adapter.setData(chordWithImages)
            })
        }
    }

}