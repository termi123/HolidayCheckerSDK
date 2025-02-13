package com.example.services.utils

enum class HolidayCheckMode {
    ANY,       // If any API reports it as a holiday, return true
    ALL,       // If any API says it’s not a holiday, return false
    CONSENSUS  // If the majority of APIs agree, return true
}