package com.example.guitarchordassistant.data

import androidx.lifecycle.LiveData

class ChordRepository(private val chordDao: ChordDao) {

    val readAllData: LiveData<List<ChordWithImages>> = chordDao.readAllData()

    suspend fun insertChord(chord: Chord){
        chordDao.insertChord(chord)
    }

    suspend fun insertImage(image: Image){
        chordDao.insertImage(image)
    }
}