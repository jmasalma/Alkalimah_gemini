package com.example.alkalimah.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.alkalimah.data.WordDao
import com.example.alkalimah.data.WordEntity

@Database(entities = [WordEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
}
