package com.example.services.datasource

import com.example.services.db.HolidayDatabase
import com.example.services.db.HolidayEntity
import com.example.services.utils.HolidayCheckMode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HolidayCacheDataSourceImpl @Inject constructor(database: HolidayDatabase) :
    HolidayCacheDataSource {
    private val holidayDao = database.holidayDao()

    override suspend fun getCachedHoliday(
        year: Int,
        month: Int,
        day: Int,
        mode: HolidayCheckMode
    ): Boolean? {
        return holidayDao.getHoliday(year, month, day, mode)
    }

    override suspend fun saveHoliday(
        year: Int,
        month: Int,
        day: Int,
        isHoliday: Boolean,
        mode: HolidayCheckMode
    ) {
        holidayDao.insertHoliday(
            HolidayEntity(
                year = year,
                month = month,
                day = day,
                isHoliday = isHoliday,
                mode = mode
            )
        )
    }
}