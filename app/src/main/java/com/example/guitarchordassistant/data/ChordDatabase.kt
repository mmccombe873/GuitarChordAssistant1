package com.example.guitarchordassistant.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Create an abstract database class containing a companion object with a
 * getDatabase function which builds a database
 */
@Database(entities = [Chord::class, Image::class], version = 2, exportSchema = false)
abstract class ChordDatabase : RoomDatabase() {

    abstract fun chordDao(): ChordDao

    companion object {
        @Volatile
        private var INSTANCE: ChordDatabase? = null

        fun getDatabase(context: Context): ChordDatabase{
            synchronized(this){
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ChordDatabase::class.java,
                    "chord_database"
                )//.createFromAsset("data/chords2.db")
                    .fallbackToDestructiveMigration()
                    .build().also {
                    INSTANCE = it
                }
            }
        }
    }
}