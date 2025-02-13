package com.example.services

import com.example.services.datasource.HolidayRemoteDataSourceImpl
import com.example.services.remote.AbstractHolidayService
import com.example.services.remote.CalendarificService
import com.example.services.remote.HolidayApiService
import com.example.services.remote.toStandardHolidays
import com.example.services.utils.HolidayCheckMode
import com.example.services.utils.HolidayResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class HolidayRemoteDataSourceTest {

    private lateinit var dataSource: HolidayRemoteDataSourceImpl

    private var calendarificService: CalendarificService = mockk()
    private var abstractApiService: AbstractHolidayService = mockk()
    private var holidayApiService: HolidayApiService = mockk()

    @Before
    fun setUp() {
        dataSource =
            HolidayRemoteDataSourceImpl(calendarificService, abstractApiService, holidayApiService)
    }

    @Test
    fun `fetchHolidays returns fail when API response has holidays`() = runTest {
        coEvery {
            calendarificService.getHolidays(
                "4VCiXN8MrAyECyH7hkvlCv9VSR4TlbSb",
                "US",
                2024,
                12,
                25
            ).body()?.toStandardHolidays()
        }.returns(
            arrayListOf()
        )

        coEvery {
            abstractApiService.getHolidays(
                "4d1d8bbd06384698ba7c950cfbc30253",
                "US",
                2024,
                12,
                25
            ).body()?.toStandardHolidays()
        }.returns(
            arrayListOf()
        )

        coEvery {
            holidayApiService.getHolidays(
                "3905bd40-e910-49cb-a2f7-2984f22eb02d",
                "US",
                2024,
                12,
                25
            ).body()?.toStandardHolidays()
        }.returns(
            arrayListOf()
        )


        // When fetching holidays
        val result = dataSource.fetchHolidays(2024, 12, 25, HolidayCheckMode.ANY)

        // Then result should be true
        assert(result == HolidayResult.Success(false))
    }

    @Test
    fun `fetchHolidays returns fail when calendarific API null`() = runTest {
        coEvery {
            calendarificService.getHolidays(
                "4VCiXN8MrAyECyH7hkvlCv9VSR4TlbSb",
                "US",
                2024,
                12,
                25
            ).body()?.toStandardHolidays()
        }.returns(
            null
        )

        coEvery {
            abstractApiService.getHolidays(
                "4d1d8bbd06384698ba7c950cfbc30253",
                "US",
                2024,
                12,
                25
            ).body()?.toStandardHolidays()
        }.returns(
            arrayListOf()
        )

        coEvery {
            holidayApiService.getHolidays(
                "3905bd40-e910-49cb-a2f7-2984f22eb02d",
                "US",
                2024,
                12,
                25
            ).body()?.toStandardHolidays()
        }.returns(
            arrayListOf()
        )


        // When fetching holidays
        val result = dataSource.fetchHolidays(2024, 12, 25, HolidayCheckMode.ALL)

        // Then result should be true
        assert(result == HolidayResult.Success(false))
    }

    @Test
    fun `fetchHolidays returns fail when abstractApiService API null`() = runTest {
        coEvery {
            calendarificService.getHolidays(
                "4VCiXN8MrAyECyH7hkvlCv9VSR4TlbSb",
                "US",
                2024,
                12,
                25
            ).body()?.toStandardHolidays()
        }.returns(
            arrayListOf()
        )

        coEvery {
            abstractApiService.getHolidays(
                "4d1d8bbd06384698ba7c950cfbc30253",
                "US",
                2024,
                12,
                25
            ).body()?.toStandardHolidays()
        }.returns(
            null
        )

        coEvery {
            holidayApiService.getHolidays(
                "3905bd40-e910-49cb-a2f7-2984f22eb02d",
                "US",
                2024,
                12,
                25
            ).body()?.toStandardHolidays()
        }.returns(
            arrayListOf()
        )


        // When fetching holidays
        val result = dataSource.fetchHolidays(2024, 12, 25, HolidayCheckMode.ALL)

        // Then result should be true
        assert(result == HolidayResult.Success(false))
    }

    @Test
    fun `fetchHolidays returns fail when holidayApiService API null`() = runTest {
        coEvery {
            calendarificService.getHolidays(
                "4VCiXN8MrAyECyH7hkvlCv9VSR4TlbSb",
                "US",
                2024,
                12,
                25
            ).body()?.toStandardHolidays()
        }.returns(
            arrayListOf()
        )

        coEvery {
            abstractApiService.getHolidays(
                "4d1d8bbd06384698ba7c950cfbc30253",
                "US",
                2024,
                12,
                25
            ).body()?.toStandardHolidays()
        }.returns(
            arrayListOf()
        )

        coEvery {
            holidayApiService.getHolidays(
                "3905bd40-e910-49cb-a2f7-2984f22eb02d",
                "US",
                2024,
                12,
                25
            ).body()?.toStandardHolidays()
        }.returns(
            null
        )


        // When fetching holidays
        val result = dataSource.fetchHolidays(2024, 12, 25, HolidayCheckMode.ALL)

        // Then result should be true
        assert(result == HolidayResult.Success(false))
    }
}
