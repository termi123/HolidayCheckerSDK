package com.example.holiday

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.services.usecase.HolidayUseCase
import com.example.services.utils.HolidayCheckMode
import com.example.services.utils.HolidayError
import com.example.services.utils.HolidayResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HolidayViewModel @Inject constructor(
    private val holidayUseCase: HolidayUseCase
) : ViewModel() {

    private val _holidayState = MutableLiveData<HolidayResult?>()
    val holidayState: LiveData<HolidayResult?> get() = _holidayState

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun checkHoliday(year: Int, month: Int, day: Int, mode: HolidayCheckMode) {
        viewModelScope.launch {
            try {
                validateInputs(year, month, day)
                _holidayState.postValue(HolidayResult.Loading)
                _errorMessage.value = null
                val result = holidayUseCase.execute(year, month, day, mode)
                _holidayState.postValue(result)
            } catch (e: IllegalArgumentException) {
                _errorMessage.value = e.message
                _holidayState.value = null
            }
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
