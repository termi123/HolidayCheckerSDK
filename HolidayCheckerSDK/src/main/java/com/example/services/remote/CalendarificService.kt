package com.example.services.remote

import com.example.services.entities.Holiday
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CalendarificService {
    @GET("v2/holidays")
    suspend fun getHolidays(
        @Query("api_key") apiKey: String,
        @Query("country") country: String,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int
    ): Response<CalendarificResponse>
}

data class CalendarificResponse(
    val response: CalendarificData
)

data class CalendarificData(
    val holidays: List<CalendarificHoliday>
)

data class CalendarificHoliday(
    val name: String,
    val date: CalendarificDate
)

data class CalendarificDate(
    val iso: String
)

fun CalendarificResponse?.toStandardHolidays(): List<Holiday>? {
    return this?.response?.holidays?.map { Holiday(it.name, it.date.iso) }
}