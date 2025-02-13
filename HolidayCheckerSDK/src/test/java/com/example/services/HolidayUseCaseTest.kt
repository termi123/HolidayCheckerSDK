package com.example.services

import com.example.services.repository.HolidayRepository
import com.example.services.usecase.HolidayUseCase
import com.example.services.utils.HolidayCheckMode
import com.example.services.utils.HolidayError
import com.example.services.utils.HolidayResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class HolidayUseCaseTest {

    private lateinit var useCase: HolidayUseCase
    private val repository: HolidayRepository = mockk()

    @Before
    fun setUp() {
        useCase = HolidayUseCase(repository)
    }

    @Test
    fun `execute returns true when date is a holiday`() = runTest {
        coEvery {
            repository.isHoliday(
                2024,
                12,
                25,
                HolidayCheckMode.ANY
            )
        } returns HolidayResult.Success(true)

        val result: HolidayResult = useCase.execute(2024, 12, 25, HolidayCheckMode.ANY)

        assert(result is HolidayResult.Success)
    }

    @Test
    fun `execute returns false when date is not a holiday`() = runTest {
        coEvery { repository.isHoliday(2024, 12, 26, HolidayCheckMode.ANY) } returns
                HolidayResult.Failure(
                    HolidayError.NetworkError
                )

        val result = useCase.execute(2024, 12, 26, HolidayCheckMode.ANY)

        assert(result is HolidayResult.Failure)
    }
}
