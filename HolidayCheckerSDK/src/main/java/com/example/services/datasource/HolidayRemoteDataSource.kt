package com.example.services.datasource

import com.example.services.utils.HolidayCheckMode
import com.example.services.utils.HolidayResult

interface HolidayRemoteDataSource {
    suspend fun fetchHolidays(
        year: Int,
        month: Int,
        day: Int,
        mode: HolidayCheckMode
    ): HolidayResult
}