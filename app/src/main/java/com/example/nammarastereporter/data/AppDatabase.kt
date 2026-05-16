package com.example.nammarastereporter.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Report::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reportDao(): ReportDao
}
