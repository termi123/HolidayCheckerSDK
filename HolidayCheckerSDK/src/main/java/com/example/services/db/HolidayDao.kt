package com.example.services.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.services.utils.HolidayCheckMode

@Dao
interface HolidayDao {
    @Query("SELECT isHoliday FROM holidays WHERE year = :year AND month = :month AND day = :day AND mode = :mode")
    suspend fun getHoliday(year: Int, month: Int, day: Int, mode: HolidayCheckMode): Boolean?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHoliday(holidayEntity: HolidayEntity)
}
