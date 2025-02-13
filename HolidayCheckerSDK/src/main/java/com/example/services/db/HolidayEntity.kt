package com.example.services.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.services.utils.HolidayCheckMode

@Entity(tableName = "holidays")
data class HolidayEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val year: Int,
    val month: Int,
    val day: Int,
    val isHoliday: Boolean,
    val mode: HolidayCheckMode,
)