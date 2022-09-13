package com.example.guitarchordassistant.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ChordDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChord(chord: Chord)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImage(image: Image)

    @Transaction
    @Query("SELECT * FROM Chord ORDER BY chordId ASC")
    fun readAllData(): LiveData<List<ChordWithImages>>

}