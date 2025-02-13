package com.example.services.datasource

import com.example.services.utils.HolidayCheckMode

interface HolidayCacheDataSource {
    suspend fun getCachedHoliday(year: Int, month: Int, day: Int, mode: HolidayCheckMode): Boolean?
    suspend fun saveHoliday(
        year: Int,
        month: Int,
        day: Int,
        isHoliday: Boolean,
        mode: HolidayCheckMode
    )
}