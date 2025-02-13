package com.example.services.usecase

import com.example.services.repository.HolidayRepository
import com.example.services.utils.HolidayCheckMode
import com.example.services.utils.HolidayResult
import javax.inject.Inject

class HolidayUseCase @Inject constructor(private val repository: HolidayRepository) {
    suspend fun execute(year: Int, month: Int, day: Int, mode: HolidayCheckMode): HolidayResult {
        return repository.isHoliday(year, month, day, mode)
    }
}