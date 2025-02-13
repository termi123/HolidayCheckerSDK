package com.example.holiday

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.services.usecase.HolidayUseCase
import com.example.services.utils.HolidayCheckMode
import com.example.services.utils.HolidayResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HolidayViewModel @Inject constructor(
    private val holidayUseCase: HolidayUseCase
) : ViewModel() {

    var holidayMode = HolidayCheckMode.ANY
    private val _holidayState = MutableLiveData<HolidayResult>()
    val holidayState: LiveData<HolidayResult> get() = _holidayState

    fun checkHoliday(year: Int, month: Int, day: Int) {
        viewModelScope.launch {
            _holidayState.postValue(HolidayResult.Loading)
            val result = holidayUseCase.execute(year, month, day, holidayMode)
            _holidayState.postValue(result)
        }
    }
}
