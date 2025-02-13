package com.example.services.datasource

import com.example.services.remote.AbstractHolidayService
import com.example.services.remote.CalendarificService
import com.example.services.remote.HolidayApiService
import com.example.services.remote.toStandardHolidays
import com.example.services.utils.HolidayCheckMode
import com.example.services.utils.HolidayError
import com.example.services.utils.HolidayResult
import timber.log.Timber
import javax.inject.Inject

class HolidayRemoteDataSourceImpl @Inject constructor(
    private val calendarificService: CalendarificService,
    private val abstractApiService: AbstractHolidayService,
    private val holidayApiService: HolidayApiService,
) : HolidayRemoteDataSource {

    override suspend fun fetchHolidays(
        year: Int,
        month: Int,
        day: Int,
        mode: HolidayCheckMode
    ): HolidayResult {
        return try {
            val responses = listOfNotNull(
                calendarificService.getHolidays(
                    "4VCiXN8MrAyECyH7hkvlCv9VSR4TlbSb",
                    "US",
                    year,
                    month,
                    day
                ).body()?.toStandardHolidays(),
                abstractApiService.getHolidays(
                    "4d1d8bbd06384698ba7c950cfbc30253",
                    "US",
                    year,
                    month,
                    day
                ).body()?.toStandardHolidays(),
                holidayApiService.getHolidays(
                    "3905bd40-e910-49cb-a2f7-2984f22eb02d",
                    "US",
                    year,
                    month,
                    day
                ).body()?.toStandardHolidays()
            )

            if (responses.isEmpty()) {
                Timber.e("HolidayRemoteDataSource: Invalid API response received")
                return HolidayResult.Failure(HolidayError.InvalidResponse)
            }
            val allHolidays = responses.flatten()
            val isHoliday = when (mode) {
                HolidayCheckMode.ANY -> allHolidays.isNotEmpty()
                HolidayCheckMode.ALL -> responses.all { it.isNotEmpty() }
                HolidayCheckMode.CONSENSUS -> allHolidays.size > responses.size / 2
            }
            Timber.d("HolidayRemoteDataSource: Result: $isHoliday")
            HolidayResult.Success(isHoliday)
        } catch (e: retrofit2.HttpException) {
            Timber.e("HolidayRemoteDataSource: HTTP error: ${e.code()}")
            if (e.code() == 429) {
                HolidayResult.Failure(HolidayError.ApiLimitExceeded)
            } else {
                HolidayResult.Failure(HolidayError.InvalidResponse)
            }
        } catch (e: java.io.IOException) {
            Timber.e("HolidayRemoteDataSource: Network error ${e.message}")
            HolidayResult.Failure(HolidayError.NetworkError)
        } catch (e: Exception) {
            Timber.e("HolidayRemoteDataSource: Unexpected error ${e.message}")
            HolidayResult.Failure(HolidayError.UnknownError)
        }
    }
}