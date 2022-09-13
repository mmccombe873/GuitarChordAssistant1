package com.example.guitarchordassistant.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChordViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<ChordWithImages>>
    private val repository: ChordRepository

    init {
        val chordDao = ChordDatabase.getDatabase(application).chordDao()
        repository = ChordRepository(chordDao)
        readAllData = repository.readAllData
    }

    fun insertChord(chord: Chord){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertChord(chord)
        }
    }

    fun insertImage(image: Image){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertImage(image)
        }
    }

}