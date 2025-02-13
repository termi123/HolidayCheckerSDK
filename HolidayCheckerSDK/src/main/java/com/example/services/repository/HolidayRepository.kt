package com.example.services.repository

import com.example.services.datasource.HolidayCacheDataSource
import com.example.services.datasource.HolidayRemoteDataSource
import com.example.services.utils.HolidayCheckMode
import com.example.services.utils.HolidayResult
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HolidayRepository @Inject constructor(
    private val remoteDataSource: HolidayRemoteDataSource,
    private val cacheDataSource: HolidayCacheDataSource
) {
    suspend fun isHoliday(year: Int, month: Int, day: Int, mode: HolidayCheckMode): HolidayResult {
        Timber.d("Fetching holidays for $year-$month-$day with mode $mode")

        val cachedResult = cacheDataSource.getCachedHoliday(year, month, day, mode)
        if (cachedResult != null) {
            Timber.d("HolidayRepository data has already in DB")
            return HolidayResult.Success(cachedResult)
        }
        Timber.d("HolidayRepository fetch from services")
        val result = remoteDataSource.fetchHolidays(year, month, day, mode)
        if (result is HolidayResult.Success) {
            cacheDataSource.saveHoliday(year, month, day, result.isHoliday, mode)
        }
        return result
    }
}
