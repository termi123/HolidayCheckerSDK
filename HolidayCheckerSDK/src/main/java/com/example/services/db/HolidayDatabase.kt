package com.example.services.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HolidayEntity::class], version = 1)
abstract class HolidayDatabase : RoomDatabase() {
    abstract fun holidayDao(): HolidayDao
}
