package com.example.services.utils

sealed class HolidayResult {
    object Loading : HolidayResult()
    data class Success(val isHoliday: Boolean) : HolidayResult()
    data class Failure(val error: HolidayError) : HolidayResult()
}

sealed class HolidayError(val message: String) {
    object NetworkError : HolidayError("Network connection issue")
    object InvalidResponse : HolidayError("Invalid API response format")
    object ApiLimitExceeded : HolidayError("API request limit exceeded")
    object UnknownError : HolidayError("An unexpected error occurred")
    data class ValidateError(val mess: String) : HolidayError(mess)
}