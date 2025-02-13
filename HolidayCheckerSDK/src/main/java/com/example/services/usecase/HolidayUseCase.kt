package com.example.services.usecase

import com.example.services.repository.HolidayRepository
import com.example.services.utils.HolidayCheckMode
import com.example.services.utils.HolidayError
import com.example.services.utils.HolidayResult
import javax.inject.Inject

class HolidayUseCase @Inject constructor(private val repository: HolidayRepository) {
    suspend fun execute(year: Int, month: Int, day: Int, mode: HolidayCheckMode): HolidayResult {
        try {
            validateInputs(year, month, day)
            return repository.isHoliday(year, month, day, mode)
        } catch (e: Exception) {
            return HolidayResult.Failure(HolidayError.ValidateError(e.message ?: ""))
        }
    }

    private fun validateInputs(year: Int, month: Int, day: Int) {
        require(year in 0..3000) { "Year must be between 0 and 3000" }
        require(month in 1..12) { "Month must be between 1 and 12" }

        val maxDaysInMonth = when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (isLeapYear(year)) 29 else 28
            else -> throw IllegalArgumentException("Invalid month: $month")
        }

        require(day in 1..maxDaysInMonth) { "Invalid day for the given month and year" }
    }

    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }
}