package com.example.services

import com.example.services.datasource.HolidayCacheDataSource
import com.example.services.datasource.HolidayRemoteDataSource
import com.example.services.repository.HolidayRepository
import com.example.services.utils.HolidayCheckMode
import com.example.services.utils.HolidayResult
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class HolidayRepositoryTest {

    private lateinit var repository: HolidayRepository
    private val remoteDataSource: HolidayRemoteDataSource = mockk()
    private val cacheDataSource: HolidayCacheDataSource = mockk()

    @Before
    fun setUp() {
        repository = HolidayRepository(remoteDataSource, cacheDataSource)
    }

    @Test
    fun `when holiday is cached, return cached result`() = runTest {
        coEvery { cacheDataSource.getCachedHoliday(2024, 12, 25, HolidayCheckMode.ANY) }.returns(
            true
        )

        val result = repository.isHoliday(2024, 12, 25, HolidayCheckMode.ANY)

        assert(result is HolidayResult.Success)
    }

    @Test
    fun `when holiday is not cached, fetch from remote and cache result`() = runTest {
        coEvery { cacheDataSource.getCachedHoliday(2024, 12, 25, HolidayCheckMode.ANY) }.returns(
            null
        )
        coEvery { remoteDataSource.fetchHolidays(2024, 12, 25, HolidayCheckMode.ANY) }.returns(
            HolidayResult.Success(true)
        )

        coEvery { cacheDataSource.saveHoliday(2024, 12, 25, true, HolidayCheckMode.ANY) } just Runs

        val result = repository.isHoliday(2024, 12, 25, HolidayCheckMode.ANY)

        assert(result is HolidayResult.Success)
    }
}
